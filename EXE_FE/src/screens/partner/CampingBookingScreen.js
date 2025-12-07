import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import BannerHome from "../../components/partner/BannerHomePartner";
import axios from "axios";
import "./CampingBookingScreen.css";

const CampingBookingScreen = () => {
  const { campingInforId } = useParams();
  const [bookings, setBookings] = useState([]);
  const [tentsMap, setTentsMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [refresh, setRefresh] = useState(false);
  const [updatingBookingId, setUpdatingBookingId] = useState(null);

  // pagination
  const [page, setPage] = useState(0);
  const [size] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchBookings = async () => {
      setLoading(true);
      setError(null);
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(
          `http://localhost:8080/api/v1/camping/booking/${campingInforId}?page=${page}&size=${size}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        const data = res.data;
        const bookingsData = data.content || [];
        setBookings(bookingsData);
        setTotalPages(data.totalPages || 0);

        // Lấy thông tin lều
        const tentPromises = bookingsData.map((b) =>
          axios.get(
            `http://localhost:8080/api/tents/byTentId/${b.campingTentId}`
          )
        );
        const tentResponses = await Promise.all(tentPromises);
        const tentMapData = {};
        tentResponses.forEach((r) => {
          if (r.data && r.data.id) tentMapData[r.data.id] = r.data;
        });
        setTentsMap(tentMapData);
      } catch (err) {
        console.error("Lỗi khi load booking:", err);
        setError("Không tải được danh sách booking");
      } finally {
        setLoading(false);
      }
    };
    fetchBookings();
  }, [campingInforId, page, size, refresh]);

  // Format helper
  const fmtDate = (iso) =>
    iso ? new Date(iso).toLocaleString("vi-VN", { hour12: false }) : "-";
  const fmtPrice = (val) =>
    val === null || val === undefined
      ? "-"
      : `${Number(val).toLocaleString("vi-VN")} VND`;

  // ✅ Hàm cập nhật trạng thái booking theo đúng logic backend
  const handleUpdateStatus = async (bookingId, currentStatus, newStatus) => {
    // Tìm booking trong danh sách để lấy status thực tế
    const booking = bookings.find(b => b.bookingId === bookingId);
    const actualStatus = booking?.status || currentStatus;
    
    console.log(`[DEBUG] Attempting to update booking ${bookingId}:`);
    console.log(`  - Current status from UI: ${currentStatus}`);
    console.log(`  - Actual status from data: ${actualStatus}`);
    console.log(`  - Target status: ${newStatus}`);
    
    // Validation theo logic backend - sử dụng actualStatus
    if (newStatus === "CONFIRMED" && actualStatus !== "PENDING") {
      alert(`Chỉ có thể xác nhận booking ở trạng thái PENDING! Trạng thái hiện tại: ${actualStatus}`);
      return;
    }

    if (newStatus === "COMPLETED" && actualStatus !== "CONFIRMED") {
      alert(`Chỉ có thể hoàn tất booking ở trạng thái CONFIRMED! Trạng thái hiện tại: ${actualStatus}`);
      return;
    }

    if (newStatus === "CANCELLED" && actualStatus !== "PENDING" && actualStatus !== "CONFIRMED") {
      alert(`Chỉ có thể hủy booking ở trạng thái PENDING hoặc CONFIRMED! Trạng thái hiện tại: ${actualStatus}`);
      return;
    }

    const statusMessages = {
      CONFIRMED: "Xác nhận chuyển booking này sang CONFIRMED?",
      COMPLETED: "Xác nhận chuyển booking này sang COMPLETED?",
      CANCELLED: "Xác nhận hủy booking này?",
    };

    if (!window.confirm(statusMessages[newStatus] || "Xác nhận cập nhật trạng thái?")) return;

    setUpdatingBookingId(bookingId);
    
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Bạn cần đăng nhập để thực hiện thao tác này!");
        setUpdatingBookingId(null);
        return;
      }

      if (!bookingId) {
        alert("Booking ID không hợp lệ!");
        setUpdatingBookingId(null);
        return;
      }

      // Xác định endpoint dựa trên trạng thái (theo đúng controller)
      let endpoint = "";
      switch (newStatus) {
        case "CONFIRMED":
          endpoint = `http://localhost:8080/api/v1/bookings/${bookingId}/confirmed`;
          break;
        case "COMPLETED":
          endpoint = `http://localhost:8080/api/v1/bookings/${bookingId}/completed`;
          break;
        case "CANCELLED":
          endpoint = `http://localhost:8080/api/v1/bookings/${bookingId}/cancel`;
          break;
        default:
          alert("Trạng thái không hợp lệ!");
          setUpdatingBookingId(null);
          return;
      }

      console.log(`[DEBUG] Calling PUT ${endpoint}`);
      console.log(`[DEBUG] Booking ID: ${bookingId}, Current Status: ${actualStatus}, New Status: ${newStatus}`);
      
      const response = await axios.put(
        endpoint, 
        null, // Không có body
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      
      console.log(`[DEBUG] Response status:`, response.status);
      console.log(`[DEBUG] Response data:`, response.data);
      
      const successMessages = {
        CONFIRMED: "Đã xác nhận booking thành công!",
        COMPLETED: "Đã hoàn tất booking thành công!",
        CANCELLED: "Đã hủy booking thành công!",
      };
      
      alert(successMessages[newStatus] || "Cập nhật trạng thái thành công!");
      
      // Reload danh sách để đảm bảo dữ liệu đồng bộ với server
      setRefresh((r) => !r);
    } catch (err) {
      console.error("[ERROR] Lỗi khi cập nhật trạng thái:", err);
      console.error("[ERROR] Error response:", err.response?.data);
      console.error("[ERROR] Error status:", err.response?.status);
      console.error("[ERROR] Error config:", err.config);
      
      let errorMessage = "Không thể cập nhật trạng thái.";
      
      if (err.response) {
        // Server trả về lỗi
        const status = err.response.status;
        const data = err.response.data;
        
        if (status === 400) {
          // Lỗi 400 thường do validation - hiển thị message chi tiết từ server
          const serverMessage = data?.message || data?.error || "Yêu cầu không hợp lệ.";
          
          // Tạo message rõ ràng hơn
          let detailedMessage = serverMessage;
          
          // Thêm thông tin về trạng thái nếu message không rõ ràng
          if (!serverMessage.includes("status") && !serverMessage.includes("trạng thái")) {
            detailedMessage = `${serverMessage}\n\nTrạng thái hiện tại: ${actualStatus}\nTrạng thái mong muốn: ${newStatus}\n\nLưu ý: Vui lòng kiểm tra lại logic backend.`;
          }
          
          errorMessage = detailedMessage;
          
          // Nếu là lỗi validation, reload lại danh sách để lấy status mới nhất
          console.log("[DEBUG] Reloading bookings due to 400 error to get latest status");
          setRefresh((r) => !r);
        } else if (status === 401) {
          errorMessage = "Bạn chưa đăng nhập hoặc phiên đăng nhập đã hết hạn.";
        } else if (status === 403) {
          errorMessage = "Bạn không có quyền thực hiện thao tác này.";
        } else if (status === 404) {
          errorMessage = "Không tìm thấy booking.";
        } else if (status === 500) {
          errorMessage = data?.message || "Lỗi server. Vui lòng thử lại sau hoặc liên hệ quản trị viên.";
        } else {
          errorMessage = data?.message || `Lỗi ${status}: ${data?.error || "Unknown error"}`;
        }
      } else if (err.request) {
        errorMessage = "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.";
      } else {
        errorMessage = err.message || "Đã xảy ra lỗi không xác định.";
      }
      
      alert(errorMessage);
    } finally {
      setUpdatingBookingId(null);
    }
  };

  if (loading) return <p>Đang tải danh sách booking...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      <BannerHome />
      <div className="container py-4">
        <h2>Danh sách booking</h2>
        <Link
          to={`/seller/camping/${campingInforId}`}
          className="btn btn-secondary mb-3"
        >
          ← Quay lại camping
        </Link>

        {bookings.length === 0 ? (
          <p style={{ textAlign: "center", color: "gray" }}>
            Chưa có booking nào.
          </p>
        ) : (
          <>
            <table className="table table-bordered table-striped">
              <thead>
                <tr>
                  <th>Booking No</th>
                  <th>Tên người dùng</th>
                  <th>Lều</th>
                  <th>Dịch vụ</th>
                  <th>Thời gian bắt đầu</th>
                  <th>Thời gian kết thúc</th>
                  <th>Tổng giá</th>
                  <th>Trạng thái</th>
                  <th>Thời gian tạo</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {bookings.map((b, index) => {
                  const tent = tentsMap[b.campingTentId] || {};
                  return (
                    <tr key={b.bookingId}>
                      <td>{index + 1}</td>
                      <td>{b.userName || "-"}</td>
                      <td>{tent.tentName || "-"}</td>
                      <td>
                        {b.serviceNames?.length > 0
                          ? b.serviceNames.join(", ")
                          : "-"}
                      </td>
                      <td>{fmtDate(b.startTime)}</td>
                      <td>{fmtDate(b.endTime)}</td>
                      <td>{fmtPrice(b.totalPrice)}</td>
                      <td
                        className={
                          b.status === "PENDING"
                            ? "status-pending"
                            : b.status === "CONFIRMED"
                            ? "status-confirmed"
                            : b.status === "COMPLETED"
                            ? "status-completed"
                            : "status-cancelled"
                        }
                      >
                        {b.status === "PENDING"
                          ? "⏳ Đang xử lý"
                          : b.status === "CONFIRMED"
                          ? "✅ Đã xác nhận"
                          : b.status === "COMPLETED"
                          ? "✅ Hoàn tất"
                          : "❌ Đã hủy"}
                      </td>
                      <td>{fmtDate(b.createdAt)}</td>
                      <td>
                        <div style={{ display: "flex", gap: "5px", flexWrap: "wrap" }}>
                          {b.status === "PENDING" && (
                            <>
                              <button
                                className="btn btn-primary btn-sm"
                                onClick={() => handleUpdateStatus(b.bookingId, b.status, "CONFIRMED")}
                                disabled={updatingBookingId === b.bookingId}
                                title="Xác nhận booking"
                              >
                                {updatingBookingId === b.bookingId ? "Đang xử lý..." : "Xác nhận"}
                              </button>
                              <button
                                className="btn btn-danger btn-sm"
                                onClick={() => handleUpdateStatus(b.bookingId, b.status, "CANCELLED")}
                                disabled={updatingBookingId === b.bookingId}
                                title="Hủy booking"
                              >
                                {updatingBookingId === b.bookingId ? "Đang xử lý..." : "Hủy"}
                              </button>
                            </>
                          )}
                          {b.status === "CONFIRMED" && (
                            <>
                              <button
                                className="btn btn-success btn-sm"
                                onClick={() => handleUpdateStatus(b.bookingId, b.status, "COMPLETED")}
                                disabled={updatingBookingId === b.bookingId}
                                title="Hoàn tất booking"
                              >
                                {updatingBookingId === b.bookingId ? "Đang xử lý..." : "Hoàn tất"}
                              </button>
                              <button
                                className="btn btn-danger btn-sm"
                                onClick={() => handleUpdateStatus(b.bookingId, b.status, "CANCELLED")}
                                disabled={updatingBookingId === b.bookingId}
                                title="Hủy booking"
                              >
                                {updatingBookingId === b.bookingId ? "Đang xử lý..." : "Hủy"}
                              </button>
                            </>
                          )}
                          {b.status === "COMPLETED" && (
                            <span className="badge bg-success">✅ Đã hoàn tất</span>
                          )}
                          {b.status === "CANCELLED" && (
                            <span className="badge bg-secondary">❌ Đã hủy</span>
                          )}
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>

            {/* Pagination */}
            <div className="d-flex justify-content-center mt-3">
              <button
                className="btn btn-outline-primary mx-2"
                disabled={page === 0}
                onClick={() => setPage((p) => p - 1)}
              >
                ← Trang trước
              </button>
              <span>
                Trang {page + 1} / {totalPages}
              </span>
              <button
                className="btn btn-outline-primary mx-2"
                disabled={page + 1 >= totalPages}
                onClick={() => setPage((p) => p + 1)}
              >
                Trang sau →
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default CampingBookingScreen;

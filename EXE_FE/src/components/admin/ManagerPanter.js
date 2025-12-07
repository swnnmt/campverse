"use client";

import Swal from "sweetalert2";
import { useState, useEffect } from "react";
import { getUsersByRole, banUser } from "../../api/adminService";
import { getPartnerDetail } from "../../api/partnerRequestService";
import "./ManagerPanter.css";

const ManagerPartner = () => {
  const [partners, setPartners] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");
  const [selectedPartner, setSelectedPartner] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [pageSize, setPageSize] = useState(6);
  const [zoomImage, setZoomImage] = useState(null);

  useEffect(() => {
    fetchPartners(currentPage);
  }, [currentPage, pageSize]);

  const fetchPartners = async (page = 0) => {
    setLoading(true);
    try {
      const response = await getUsersByRole("partner", page, pageSize);
      setPartners(response.data.content || []);
      setTotalPages(response.data.totalPages || 0);
    } catch (error) {
      console.error("Lỗi khi tải danh sách partner:", error);
      setError("Không thể tải dữ liệu từ server.");
    } finally {
      setLoading(false);
    }
  };

  const handleBanToggle = async (partnerId, isBanned, fullName) => {
    const action = !isBanned ? "khóa" : "mở khóa";
    const actioned = !isBanned ? "bị khóa" : "được mở khóa";
    const icon = !isBanned ? "warning" : "question";
    const confirmButtonText = !isBanned ? "🚫 Khóa" : "✅ Mở khóa";

    const result = await Swal.fire({
      title: `Xác nhận ${action}`,
      text: `Bạn có chắc chắn muốn ${action} đối tác "${fullName}" không?`,
      icon,
      showCancelButton: true,
      confirmButtonColor: !isBanned ? "#d33" : "#3085d6",
      cancelButtonColor: "#6c757d",
      confirmButtonText,
      cancelButtonText: "Hủy",
    });

    if (!result.isConfirmed) return;

    try {
      await banUser(partnerId, !isBanned);
      setPartners((prev) =>
        prev.map((p) => (p.id === partnerId ? { ...p, locked: !isBanned } : p))
      );

      Swal.fire({
        icon: "success",
        title: "Thành công",
        text: `Đối tác "${fullName}" đã ${actioned}!`,
        confirmButtonText: "OK",
      });
    } catch (error) {
      console.error("Lỗi khi ban/unban partner:", error);
      Swal.fire({
        icon: "error",
        title: "Thất bại",
        text: "❌ Thao tác thất bại, vui lòng thử lại!",
        confirmButtonText: "Đóng",
      });
    }
  };

  const filteredPartners = partners.filter((p) => {
    const matchesSearch =
      p.fullName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.email?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus =
      statusFilter === "all" ||
      (statusFilter === "active" && !p.locked) ||
      (statusFilter === "locked" && p.locked);
    return matchesSearch && matchesStatus;
  });

  if (loading) return <div className="loading">Đang tải...</div>;

  return (
    <div className="manager-partner-container">
      {/* Thông báo lỗi */}
      {error && (
        <div
          style={{
            backgroundColor: "#fff3cd",
            border: "1px solid #ffeaa7",
            color: "#856404",
            padding: "12px",
            borderRadius: "4px",
            marginBottom: "20px",
          }}
        >
          ⚠️ {error}
        </div>
      )}

      {/* Header */}
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginBottom: "20px",
          alignItems: "center",
        }}
      >
        <h1 style={{ fontSize: "24px", fontWeight: "bold", color: "#2c3e50" }}>
          📋 Quản Lý Đối Tác Camping
        </h1>
        <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
          <input
            type="text"
            placeholder="🔍 Tìm kiếm đối tác..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            style={{
              padding: "8px 12px",
              borderRadius: "6px",
              border: "1px solid #ccc",
              minWidth: "220px",
            }}
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            style={{
              padding: "8px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          >
            <option value="all">Tất cả</option>
            <option value="active">Hoạt động</option>
            <option value="locked">Bị khóa</option>
          </select>
          <select
            value={pageSize}
            onChange={(e) => {
              setPageSize(Number(e.target.value));
              setCurrentPage(0);
            }}
            style={{
              padding: "8px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          >
            <option value={6}>6 / trang</option>
            <option value={10}>10 / trang</option>
            <option value={20}>20 / trang</option>
            <option value={50}>50 / trang</option>
          </select>
        </div>
      </div>

      {/* Bảng danh sách */}
      <div style={{ overflowX: "auto" }}>
        <table className="mp-table">
          <thead className="mp-thead">
            <tr>
              <th className="mp-th">STT</th>
              <th className="mp-th">Tên đối tác</th>
              <th className="mp-th">Email</th>
              <th className="mp-th">SĐT</th>
              <th className="mp-th">Trạng Thái</th>
              <th className="mp-th">Hành động</th>
            </tr>
          </thead>
          <tbody>
            {filteredPartners.length > 0 ? (
              filteredPartners.map((p, index) => (
                <tr key={p.id} className="mp-row">
                  <td className="mp-td">
                    {currentPage * pageSize + index + 1}
                  </td>
                  <td
                    className="mp-td mp-link"
                    onClick={async () => {
                      try {
                        const detail = await getPartnerDetail(p.id);
                        setSelectedPartner(detail);
                      } catch (err) {
                        console.error("Không tải được chi tiết partner:", err);
                      }
                    }}
                  >
                    {p.fullName}
                  </td>
                  <td className="mp-td">{p.email}</td>
                  <td className="mp-td">{p.phoneNumber}</td>
                  <td className="mp-td">
                    <span
                      className="mp-status"
                      style={{ background: p.locked ? "#e74c3c" : "#2ecc71" }}
                    >
                      {p.locked ? "Bị khóa" : "Hoạt động"}
                    </span>
                  </td>
                  <td className="mp-td">
                    <button
                      onClick={() =>
                        handleBanToggle(p.id, p.locked, p.fullName)
                      }
                      className={
                        "mp-action-btn " + (p.locked ? "unlock" : "lock")
                      }
                    >
                      {p.locked ? "Mở khóa" : "Khóa"}
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="mp-empty">
                  Không tìm thấy đối tác nào
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      {/* Pagination */}
      <div className="mp-pagination" style={{ marginTop: "30px" }}>
        <button
          disabled={currentPage === 0}
          onClick={() => setCurrentPage((prev) => prev - 1)}
        >
          &lt; Trước
        </button>
        <span>
          {currentPage + 1} / {totalPages}
        </span>
        <button
          disabled={currentPage + 1 >= totalPages}
          onClick={() => setCurrentPage((prev) => prev + 1)}
        >
          Tiếp &gt;
        </button>
      </div>

      {/* Popup chi tiết partner */}
      {selectedPartner && (
        <div
          style={{
            position: "fixed",
            inset: 0,
            backgroundColor: "rgba(0,0,0,0.6)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            zIndex: 9999,
            padding: "20px",
            overflowY: "auto",
          }}
          onClick={() => setSelectedPartner(null)}
        >
          <div
            style={{
              background: "#fff",
              borderRadius: "12px",
              maxWidth: "900px",
              width: "100%",
              maxHeight: "90vh",
              overflowY: "auto",
              position: "relative",
              boxShadow: "0 0 20px rgba(0,0,0,0.3)",
            }}
            onClick={(e) => e.stopPropagation()}
          >
            <button
              style={{
                position: "absolute",
                top: "10px",
                right: "10px",
                background: "transparent",
                border: "none",
                fontSize: "20px",
                cursor: "pointer",
              }}
              onClick={() => setSelectedPartner(null)}
            >
              ✖
            </button>

            <div style={{ padding: "20px" }}>
              {/* Thông tin partner */}
              <h3 style={{ marginBottom: "10px", color: "#2c3e50" }}>
                {selectedPartner.fullName}
              </h3>
              <p>
                <strong>Email:</strong> {selectedPartner.email}
              </p>
              <p>
                <strong>SĐT:</strong> {selectedPartner.phoneNumber}
              </p>
              <p>
                <strong>Trạng thái:</strong>{" "}
                {selectedPartner.locked ? "Bị khóa" : "Hoạt động"}
              </p>

              {/* Camping sites */}
              <h5 style={{ marginTop: "20px", color: "#27ae60" }}>
                🏕️ Các điểm cắm trại
              </h5>
              {selectedPartner.campingSites?.length > 0 ? (
                selectedPartner.campingSites.map((site) => (
                  <div
                    key={site.id}
                    style={{
                      marginBottom: "15px",
                      border: "1px solid #ddd",
                      borderRadius: "8px",
                      padding: "10px",
                    }}
                  >
                    <h6 style={{ color: "#2980b9" }}>{site.name}</h6>
                    <p style={{ margin: "2px 0" }}>📍 {site.location}</p>
                    <p style={{ margin: "2px 0" }}>📝 {site.description}</p>

                    {site.images?.length > 0 && (
                      <div
                        style={{
                          display: "flex",
                          gap: "8px",
                          flexWrap: "wrap",
                        }}
                      >
                        {site.images.map((img, i) => (
                          <img
                            key={i}
                            src={img}
                            alt={`${site.name}-${i}`}
                            style={{
                              width: "80px",
                              height: "80px",
                              objectFit: "cover",
                              cursor: "pointer",
                              borderRadius: "6px",
                            }}
                            onClick={() => setZoomImage(img)}
                          />
                        ))}
                      </div>
                    )}
                  </div>
                ))
              ) : (
                <p>Chưa có điểm cắm trại nào.</p>
              )}
            </div>
          </div>
        </div>
      )}

      {/* Zoom ảnh */}
      {zoomImage && (
        <div
          className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center bg-dark bg-opacity-75"
          style={{ zIndex: 1055 }} // > Bootstrap modal
          onClick={() => setZoomImage(null)}
        >
          <img
            src={zoomImage}
            alt="Zoomed"
            style={{
              maxWidth: "90%",
              maxHeight: "90%",
              borderRadius: "10px",
              boxShadow: "0 0 20px rgba(255,255,255,0.4)",
            }}
          />
        </div>
      )}
    </div>
  );
};

export default ManagerPartner;

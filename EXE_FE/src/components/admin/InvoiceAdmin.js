import { useEffect, useState } from "react";
import {
  getAllBookings,
  searchInvoices,
  getInvoiceById,
  updateInvoiceStatus,
  deleteInvoice,
} from "../../api/invoiceAdminService";
import InvoiceDetailModal from "./InvoiceDetailModal";
import "./InvoiceAdmin.css";

// Hàm debounce
function useDebounce(value, delay) {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    const handler = setTimeout(() => setDebouncedValue(value), delay);
    return () => clearTimeout(handler);
  }, [value, delay]);

  return debouncedValue;
}

export default function InvoiceAdmin() {
  const [invoices, setInvoices] = useState([]);
  const [selectedInvoice, setSelectedInvoice] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [filterStatus, setFilterStatus] = useState("ALL");

  // Pagination
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10); // số item mỗi trang
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0); // tổng số hóa đơn

  // Search
  const [searchId, setSearchId] = useState("");
  const [startDate, setStartDate] = useState(""); // datetime-local string
  const [endDate, setEndDate] = useState(""); // datetime-local string
  const debouncedSearchId = useDebounce(searchId, 500); // 500ms debounce
  const debouncedStart = useDebounce(startDate, 500);
  const debouncedEnd = useDebounce(endDate, 500);

  // Map trạng thái sang tiếng Việt
  const statusLabels = {
    COMPLETED: "Hoàn thành",
    CONFIRMED: "Đã đặt",
    CANCELLED: "Đã hủy",
    PENDING: "Chờ thanh toán",
  };

  useEffect(() => {
    fetchInvoices(page, size);
  }, [page, size, debouncedSearchId, debouncedStart, debouncedEnd]);

  const fetchInvoices = async (pageNumber = 0, pageSize = size) => {
    try {
      let data;
      const hasId = debouncedSearchId && debouncedSearchId.trim().length > 0;
      const hasRange = debouncedStart && debouncedEnd;
      if (hasId || hasRange) {
        const params = {
          page: pageNumber,
          size: pageSize,
        };
        if (hasId) params.bookingId = debouncedSearchId.trim();
        if (hasRange) {
          // Backend expects ISO DATE_TIME
          params.start = new Date(debouncedStart).toISOString();
          params.end = new Date(debouncedEnd).toISOString();
        }
        data = await searchInvoices(params);
      } else {
        data = await getAllBookings(pageNumber, pageSize);
      }

      setInvoices(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.error("Error fetching invoices:", err);
    }
  };

  const handleShowDetail = async (invoiceId) => {
    try {
      const data = await getInvoiceById(invoiceId);
      setSelectedInvoice(data);
      setShowModal(true);
    } catch (err) {
      console.error("Error fetching invoice detail:", err);
    }
  };

  const handleUpdateStatus = async (invoiceId, status) => {
    try {
      await updateInvoiceStatus(invoiceId, status);
      await fetchInvoices(page, size);
    } catch (err) {
      console.error("Error updating status:", err);
    }
  };

  const handleDelete = async (invoiceId) => {
    try {
      await deleteInvoice(invoiceId);
      // after delete, refresh current page or move back a page if empty
      await fetchInvoices(page, size);
    } catch (err) {
      console.error("Error deleting invoice:", err);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    const date = new Date(dateString);
    return date.toLocaleDateString("vi-VN", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const filteredInvoices =
    filterStatus === "ALL"
      ? invoices
      : invoices.filter((i) => i.status === filterStatus);

  return (
    <div className="invoice-admin">
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginBottom: "20px",
          alignItems: "center",
        }}
      >
        <h1 style={{ fontSize: "24px", fontWeight: "bold", color: "#2c3e50" }}>
          📑 Quản Lý Hóa Đơn
        </h1>

        <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
          <input
            type="text"
            placeholder="🔍 Tìm kiếm theo mã hóa đơn..."
            value={searchId}
            onChange={(e) => setSearchId(e.target.value)}
            style={{
              padding: "8px 12px",
              borderRadius: "6px",
              border: "1px solid #ccc",
              minWidth: "220px",
            }}
          />

          <input
            type="datetime-local"
            value={startDate}
            onChange={(e) => {
              setStartDate(e.target.value);
              setPage(0);
            }}
            style={{
              padding: "8px 12px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />
          <span>→</span>
          <input
            type="datetime-local"
            value={endDate}
            onChange={(e) => {
              setEndDate(e.target.value);
              setPage(0);
            }}
            style={{
              padding: "8px 12px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          />

          <select
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
            style={{
              padding: "8px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          >
            <option value="ALL">Tất cả</option>
            <option value="COMPLETED">Hoàn thành</option>
            <option value="CONFIRMED">Đã xác nhận</option>
            <option value="CANCELLED">Đã hủy</option>
            <option value="PENDING">Chờ xử lý</option>
          </select>

          <select
            value={size}
            onChange={(e) => {
              setSize(Number(e.target.value));
              setPage(0); // reset page về 0 khi đổi size
            }}
            style={{
              padding: "8px",
              borderRadius: "6px",
              border: "1px solid #ccc",
            }}
          >
            <option value={5}>5 / trang</option>
            <option value={10}>10 / trang</option>
            <option value={20}>20 / trang</option>
            <option value={50}>50 / trang</option>
          </select>
        </div>
      </div>

      {/* Thông tin tổng số hóa đơn */}
      <div style={{ marginBottom: "10px" }}>
        Tổng số hóa đơn: <strong>{totalElements}</strong>
      </div>

      {/* Bảng danh sách hóa đơn */}
      <table className="invoice-table">
        <thead>
          <tr>
            <th>Mã đơn hàng</th>
            <th>Khách hàng</th>
            <th>Email</th>
            <th>SĐT</th>
            <th>Camping Site</th>
            <th>Ngày đặt</th>
            <th>Trạng thái</th>
            <th>Tổng tiền</th>
          </tr>
        </thead>
        <tbody>
          {filteredInvoices.length > 0 ? (
            filteredInvoices.map((invoice) => (
              <tr key={invoice.invoiceId}>
                <td>{invoice.invoiceId}</td>
                <td>{invoice.customerName}</td>
                <td>{invoice.customerEmail}</td>
                <td>{invoice.customerPhone}</td>
                <td>
                  {invoice.campingSite
                    ? `(${invoice.campingSite.location})`
                    : "Chưa chọn"}
                </td>
                <td>{formatDate(invoice.createdAt)}</td>
                <td>
                  <span
                    className={`status-badge ${
                      invoice.status === "COMPLETED"
                        ? "completed"
                        : invoice.status === "CONFIRMED"
                        ? "confirmed"
                        : invoice.status === "CANCELLED"
                        ? "cancelled"
                        : "pending"
                    }`}
                  >
                    {statusLabels[invoice.status] || invoice.status}
                  </span>
                </td>
                <td>
                  {Number(invoice.totalPrice || 0).toLocaleString("vi-VN")} ₫
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="9" style={{ textAlign: "center", padding: "20px" }}>
                Không tìm thấy hóa đơn nào
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {/* Pagination */}
      <div className="pagination">
        <button
          disabled={page === 0}
          onClick={() => setPage((prev) => prev - 1)}
        >
          &lt; Trước
        </button>
        <span>
          {page + 1} / {totalPages}
        </span>
        <button
          disabled={page + 1 >= totalPages}
          onClick={() => setPage((prev) => prev + 1)}
        >
          Tiếp &gt;
        </button>
      </div>

      {/* Modal chi tiết */}
      {showModal && selectedInvoice && (
        <InvoiceDetailModal
          invoice={selectedInvoice}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
}

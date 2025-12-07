import "./InvoiceAdmin.css";

export default function InvoiceDetailModal({ invoice, onClose }) {
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="modal-close" onClick={onClose}>
          ✖
        </button>
        <h2 className="modal-title">Chi tiết hóa đơn {invoice.id}</h2>

        <div className="modal-body">
          {/* Thông tin khách hàng */}
          <h3 className="modal-subtitle">👤 Thông tin khách hàng</h3>
          <p><strong>Họ tên:</strong> {invoice.customerName}</p>
          <p><strong>Email:</strong> {invoice.customerEmail}</p>
          <p><strong>SĐT:</strong> {invoice.customerPhone}</p>
          <p><strong>Trạng thái:</strong> {invoice.status}</p>
          <p><strong>Tổng tiền:</strong> {Number(invoice.totalPrice || 0).toLocaleString('vi-VN')} ₫</p>
          <p><strong>Ngày tạo:</strong> {new Date(invoice.createdAt).toLocaleString('vi-VN')}</p>
          {invoice.startTime && invoice.endTime && (
            <p><strong>Thời gian đặt:</strong> {new Date(invoice.startTime).toLocaleString('vi-VN')} → {new Date(invoice.endTime).toLocaleString('vi-VN')}</p>
          )}

          {/* Thông tin camping site */}
          {invoice.campingSite && (
            <>
              <h3 className="modal-subtitle">🏕️ Camping Site</h3>
              <p><strong>Tên:</strong> {invoice.campingSite.name}</p>
              <p><strong>Mô tả:</strong> {invoice.campingSite.description}</p>
              <p><strong>Địa điểm:</strong> {invoice.campingSite.location}</p>
              <p>
                <strong>Hoạt động:</strong>{" "}
                {invoice.campingSite.isActive ? "Đang hoạt động" : "Ngừng hoạt động"}
              </p>
            </>
          )}

          {/* Chi tiết booking */}
          <h3 className="modal-subtitle">📝 Chi tiết đặt phòng</h3>
          <ul className="invoice-items">
            {invoice.details && invoice.details.length > 0 ? (
              invoice.details.map((d, index) => (
                <li key={index}>
                  <strong>{d.itemName}</strong> | Từ {d.checkIn ? new Date(d.checkIn).toLocaleDateString('vi-VN') : 'N/A'} → {d.checkOut ? new Date(d.checkOut).toLocaleDateString('vi-VN') : 'N/A'} | 💵 {Number(d.price || 0).toLocaleString('vi-VN')} ₫
                </li>
              ))
            ) : (
              <li>Không có chi tiết</li>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
}

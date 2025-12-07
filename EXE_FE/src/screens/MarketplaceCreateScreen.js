// src/screens/MarketplaceCreateScreen.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import BannerHome from "../components/BannerHome";

// ================== Cấu hình Cloudinary ==================
const CLOUDINARY_CLOUD_NAME = "dex1n6s6f"; // Thay bằng cloud name của bạn
const CLOUDINARY_UPLOAD_PRESET = "uploadCampverse"; // Unsigned preset bạn tạo

const MarketplaceCreateScreen = () => {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user")) || {};
  const [form, setForm] = useState({
    userId: user.id || "",
    phone: user.phoneNumber || "",
    facebookLink: "",
    productName: "",
    productImage: "", // link ảnh từ Cloudinary
    quantity: 1,
    price: 0,
    description: "",
  });

  const [loading, setLoading] = useState(false);
  const [uploading, setUploading] = useState(false); // trạng thái upload ảnh
  const [error, setError] = useState(null);

  // ================== Xử lý input ==================
  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  // ================== Upload ảnh lên Cloudinary ==================
  const handleImageUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    // Validation file
    if (!file.type.startsWith("image/")) {
      alert("File phải là ảnh (JPG, PNG, GIF, etc.)!");
      return;
    }

    const maxSize = 10 * 1024 * 1024; // 10MB
    if (file.size > maxSize) {
      alert("Kích thước ảnh không được vượt quá 10MB!");
      return;
    }

    const data = new FormData();
    data.append("file", file);
    data.append("upload_preset", CLOUDINARY_UPLOAD_PRESET);

    setUploading(true);
    try {
      const res = await axios.post(
        `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
        data,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      const imageUrl = res.data?.secure_url;
      if (!imageUrl) throw new Error("Không nhận được URL từ Cloudinary");

      setForm((prev) => ({ ...prev, productImage: imageUrl }));
    } catch (err) {
      console.error("Upload failed:", err.response?.data || err.message);
      alert("Lỗi khi upload ảnh. Vui lòng thử lại!");
    } finally {
      setUploading(false);
      e.target.value = ""; // reset input để chọn lại cùng file
    }
  };

  // ================== Submit form ==================
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    if (!form.productImage) {
      alert("Vui lòng chọn ảnh sản phẩm!");
      setLoading(false);
      return;
    }

    try {
      await axios.post("http://localhost:8080/api/v1/marketplace", form);
      alert("Đăng sản phẩm thành công!");
      navigate("/marketplace/my-products"); // chuyển đến trang sản phẩm của tôi
    } catch (err) {
      console.error(err);
      setError("Có lỗi xảy ra khi đăng sản phẩm.");
    } finally {
      setLoading(false);
    }
  };

  // ================== Render ==================
  return (
    <>
      <BannerHome />
      <div className="container py-4" style={{ maxWidth: "600px" }}>
        <h2 className="mb-4">Đăng sản phẩm Marketplace</h2>
        {error && <p className="text-danger">{error}</p>}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Tên sản phẩm</label>
            <input
              type="text"
              className="form-control"
              value={form.productName}
              onChange={(e) => handleChange("productName", e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Số lượng</label>
            <input
              type="number"
              min="1"
              className="form-control"
              value={form.quantity === 0 ? "" : form.quantity}
              onChange={(e) => handleChange("quantity", Number(e.target.value))}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Giá (VND)</label>
            <input
              type="number"
              min="0"
              className="form-control"
              value={form.price === 0 ? "" : form.price}
              onChange={(e) => handleChange("price", Number(e.target.value))}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Mô tả</label>
            <textarea
              className="form-control"
              value={form.description}
              onChange={(e) => handleChange("description", e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Số điện thoại</label>
            <input
              type="tel"
              className="form-control"
              value={form.phone}
              onChange={(e) => handleChange("phone", e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Link Facebook</label>
            <input
              type="url"
              className="form-control"
              value={form.facebookLink}
              onChange={(e) => handleChange("facebookLink", e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Ảnh sản phẩm</label>
            <input
              type="file"
              accept="image/*"
              className="form-control"
              onChange={handleImageUpload}
              disabled={uploading}
            />
            {uploading && <p>Đang upload ảnh vui lòng đợi...</p>}
            {form.productImage && (
              <img
                src={form.productImage}
                alt="Preview"
                className="mt-2"
                style={{ width: "100%", maxHeight: "300px", objectFit: "cover" }}
              />
            )}
          </div>

          <button type="submit" className="btn btn-primary w-100" disabled={loading || uploading}>
            {loading ? "Đang đăng..." : "Đăng sản phẩm"}
          </button>
        </form>
      </div>
    </>
  );
};

export default MarketplaceCreateScreen;

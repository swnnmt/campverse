import React, { useEffect, useState } from "react";
import communityApi from "../api/communityService";
import CommentList from "./CommentList";
import {
  FaEdit,
  FaTrash,
  FaSave,
  FaTimes,
  FaHeart,
  FaComment,
} from "react-icons/fa";
import "bootstrap/dist/css/bootstrap.min.css";
import "./MyPostsPage.css";

// Toast notification component
const ToastNotification = ({
  message,
  type = "info",
  onClose,
  duration = 3000,
}) => {
  useEffect(() => {
    const timer = setTimeout(onClose, duration);
    return () => clearTimeout(timer);
  }, [onClose, duration]);

  return <div className={`toast-notification ${type}`}>{message}</div>;
};

// 🧩 Chuyển file ảnh sang Base64
const convertFileToBase64 = (file) =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });

// 🔹 Lấy userId từ localStorage
const getUserIdFromLocalStorage = () => {
  let userId = localStorage.getItem("userId");
  if (!userId) {
    const potentialKeys = ["user_id", "currentUserId", "userInfo", "user"];
    for (const key of potentialKeys) {
      const storedValue = localStorage.getItem(key);
      if (storedValue) {
        try {
          const userData = JSON.parse(storedValue);
          if (userData && userData.id) return userData.id;
        } catch {
          if (storedValue.length > 10) return storedValue;
        }
      }
    }
  }
  return userId || null;
};

const MyPostsPage = () => {
  const userId = getUserIdFromLocalStorage();
  const [posts, setPosts] = useState([]);
  const [likes, setLikes] = useState({});
  const [comments, setComments] = useState({});
  const [showComments, setShowComments] = useState({});
  const [editingPostId, setEditingPostId] = useState(null);
  const [editContent, setEditContent] = useState("");
  const [editImage, setEditImage] = useState(null);
  const [editImagePreview, setEditImagePreview] = useState("");
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState(null); // 🔹 State Toast

  const showToast = (message, type = "info") => setToast({ message, type });

  // 🟢 Lấy danh sách bài viết
  const fetchMyPosts = async () => {
    if (!userId) return;
    try {
      const res = await communityApi.getPostsByUser(userId);
      const fetchedPosts = res.data;
      setPosts(fetchedPosts);

      fetchedPosts.forEach(async (p) => {
        const likeRes = await communityApi.countLikes(p.id);
        const commentRes = await communityApi.getCommentsByPost(p.id);
        setLikes((prev) => ({ ...prev, [p.id]: likeRes.data }));
        setComments((prev) => ({ ...prev, [p.id]: commentRes.data }));
      });
    } catch (err) {
      console.error("Lỗi khi tải bài viết:", err);
    }
  };

  useEffect(() => {
    if (userId) fetchMyPosts();
  }, [userId]);

  // ❤️ Toggle Like
  const handleLike = async (postId) => {
    try {
      await communityApi.toggleLike(postId, userId);
      const likeRes = await communityApi.countLikes(postId);
      setLikes((prev) => ({ ...prev, [postId]: likeRes.data }));
    } catch (error) {
      console.error("Lỗi khi like bài:", error);
    }
  };

  // ✏️ Bắt đầu sửa
  const handleEdit = (post) => {
    setEditingPostId(post.id);
    setEditContent(post.content);
    setEditImage(null);
    setEditImagePreview(post.imageUrl || "");
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setEditImage(file);
      const reader = new FileReader();
      reader.onloadend = () => setEditImagePreview(reader.result);
      reader.readAsDataURL(file);
    } else {
      setEditImage(null);
      setEditImagePreview("");
    }
  };

  const removeImage = () => {
    setEditImage(null);
    setEditImagePreview("");
  };

  // 💾 Lưu chỉnh sửa (với toast)
  const handleSave = async (postId) => {
    try {
      setLoading(true);
      let imageBase64 = editImage
        ? await convertFileToBase64(editImage)
        : editImagePreview || "";

      await communityApi.updatePost(postId, userId, editContent, imageBase64);
      showToast("✅ Cập nhật bài viết thành công!", "success"); // 🔹 Toast thay vì alert
      setEditingPostId(null);
      setEditContent("");
      setEditImage(null);
      setEditImagePreview("");
      fetchMyPosts();
    } catch (err) {
      console.error(err);
      showToast("❌ Lỗi khi cập nhật bài viết!", "error"); // 🔹 Toast lỗi
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (postId) => {
    if (!window.confirm("Bạn chắc chắn muốn xóa bài này?")) return;
    try {
      await communityApi.deletePost(postId, userId);
      showToast("✅ Xóa bài viết thành công!", "success");
      fetchMyPosts();
    } catch {
      showToast("❌ Lỗi khi xóa bài viết!", "error");
    }
  };

  return (
    <div className="my-posts-page container py-4">
      {/* Toast */}
      {toast && (
        <ToastNotification
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}

      <h2 className="text-center mb-4 fw-bold text-primary">
        📝 Bài viết của tôi
      </h2>

      {posts.length === 0 ? (
        <p className="text-center text-muted">Bạn chưa có bài đăng nào.</p>
      ) : (
        posts.map((post) => (
          <div
            key={post.id}
            className="post-card card mb-4 shadow-sm border-0 rounded-4"
          >
            <div className="card-body">
              {editingPostId === post.id ? (
                <>
                  <textarea
                    className="form-control mb-2"
                    value={editContent}
                    onChange={(e) => setEditContent(e.target.value)}
                    rows="3"
                  />

                  {editImagePreview && (
                    <div className="image-preview-container">
                      <img
                        src={editImagePreview}
                        alt="preview"
                        className="image-preview"
                      />
                      <button
                        type="button"
                        className="remove-image-btn"
                        onClick={removeImage}
                      >
                        ×
                      </button>
                    </div>
                  )}

                  <input
                    type="file"
                    className="form-control mb-2"
                    accept="image/*"
                    onChange={handleImageChange}
                  />

                  <div className="d-flex justify-content-end gap-2">
                    <button
                      className="btn btn-success"
                      onClick={() => handleSave(post.id)}
                      disabled={loading}
                    >
                      <FaSave /> Lưu
                    </button>
                    <button
                      className="btn btn-secondary"
                      onClick={() => setEditingPostId(null)}
                    >
                      <FaTimes /> Hủy
                    </button>
                  </div>
                </>
              ) : (
                <>
                  <p className="post-content">{post.content}</p>
                  {post.imageUrl && (
                    <img
                      src={post.imageUrl}
                      alt="Post"
                      className="post-image rounded-3 mb-3"
                    />
                  )}

                  <div className="d-flex align-items-center justify-content-between mt-2">
                    <div className="d-flex gap-3">
                      <button
                        className="btn btn-outline-danger btn-sm"
                        onClick={() => handleLike(post.id)}
                      >
                        <FaHeart className="me-1" /> {likes[post.id] || 0}
                      </button>

                      <button
                        className="btn btn-outline-secondary btn-sm"
                        onClick={() =>
                          setShowComments((prev) => ({
                            ...prev,
                            [post.id]: !prev[post.id],
                          }))
                        }
                      >
                        <FaComment className="me-1" />
                        {(comments[post.id] || []).length} Bình luận
                      </button>
                    </div>

                    <div className="d-flex gap-2">
                      <button
                        className="btn btn-outline-primary btn-sm"
                        onClick={() => handleEdit(post)}
                      >
                        <FaEdit /> Sửa
                      </button>
                      <button
                        className="btn btn-outline-danger btn-sm"
                        onClick={() => handleDelete(post.id)}
                      >
                        <FaTrash /> Xóa
                      </button>
                    </div>
                  </div>

                  {showComments[post.id] && (
                    <div className="comment-section mt-3">
                      <CommentList
                        postId={post.id}
                        comments={comments[post.id] || []}
                        currentUserId={userId}
                        onCommentAdded={fetchMyPosts}
                      />
                    </div>
                  )}
                </>
              )}
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default MyPostsPage;

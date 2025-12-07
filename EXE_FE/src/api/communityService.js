import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/community";

const communityApi = {
  // 🟢 Lấy bài đăng
  getPostsPaged: (page = 0, size = 10) => {
    return axios.get(`${API_BASE_URL}/posts`, { params: { page, size } });
  },

  // 🟢 Tạo bài đăng
  createPost: (userId, content, imageBase64) => {
    const payload = { userId, content, imageBase64 };
    return axios.post(`${API_BASE_URL}/posts`, payload, {
      headers: { "Content-Type": "application/json" },
    });
  },
  // 🟢 Lấy bài đăng theo user
  getPostsByUser: (userId) => {
    return axios.get(`${API_BASE_URL}/posts/user/${userId}`);
  },

  // ✏️ Cập nhật bài đăng
  updatePost: (postId, userId, content, imageBase64) => {
    return axios.put(
      `${API_BASE_URL}/posts/${postId}`,
      {
        userId,
        content,
        imageBase64,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  // ❌ Xóa bài đăng
  deletePost: (postId, userId) => {
    return axios.delete(`${API_BASE_URL}/posts/${postId}`, {
      params: { userId },
    });
  },

  // ❤️ Bật/Tắt like
  toggleLike: (postId, userId) => {
    const params = new URLSearchParams();
    params.append("postId", postId);
    params.append("userId", userId);
    return axios.post(`${API_BASE_URL}/likes/toggle`, null, { params });
  },

  // ❤️ Đếm like
  countLikes: (postId) => {
    return axios.get(`${API_BASE_URL}/likes/count/${postId}`);
  },

  // 💬 Lấy comment theo bài viết
  getCommentsByPost: (postId) => {
    return axios.get(`${API_BASE_URL}/comments/${postId}`);
  },

  // 💬 Thêm comment mới
  addComment: (postId, userId, content) => {
    const body = { postId, userId, content };
    return axios.post(`${API_BASE_URL}/comments`, body);
  },

  // 🟣 Thêm phản hồi (reply)
  replyToComment: (postId, userId, content, parentId) => {
    const body = { postId, userId, content, parentId };
    return axios.post(`${API_BASE_URL}/comments/reply`, body, {
      headers: { "Content-Type": "application/json" },
    });
  },
};

export default communityApi;

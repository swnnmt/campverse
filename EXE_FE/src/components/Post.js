import React, { useState, useEffect } from "react";
import PostFooter from "./PostFooter";
import CommentList from "./CommentList";
import communityApi from "../api/communityService";
import "../components/css/Post.css";

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

const Post = ({ post, currentUserId }) => {
  const isLoggedIn = !!currentUserId;

  const [likesCount, setLikesCount] = useState(0);
  const [commentsCount, setCommentsCount] = useState(0);
  const [comments, setComments] = useState([]);
  const [showComments, setShowComments] = useState(false);
  const [toast, setToast] = useState(null);

  const showToast = (message, type = "info") => setToast({ message, type });

  const fetchPostStats = async () => {
    try {
      const likesResponse = await communityApi.countLikes(post.id);
      setLikesCount(likesResponse.data);

      const commentsResponse = await communityApi.getCommentsByPost(post.id);
      setCommentsCount(commentsResponse.data.length);
      if (showComments) setComments(commentsResponse.data);
    } catch (error) {
      console.error("Lỗi tải thống kê bài viết:", error);
    }
  };

  useEffect(() => {
    fetchPostStats();
  }, [post.id]);

  const handleToggleLike = async () => {
    if (!isLoggedIn) {
      showToast("⚠️ Bạn cần đăng nhập để thích bài viết!", "error");
      return;
    }
    try {
      await communityApi.toggleLike(post.id, currentUserId);
      const response = await communityApi.countLikes(post.id);
      setLikesCount(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleToggleComments = async () => {
    if (!showComments) {
      try {
        const res = await communityApi.getCommentsByPost(post.id);
        setComments(res.data);
        setCommentsCount(res.data.length);
      } catch (error) {
        console.error(error);
      }
    }
    setShowComments(!showComments);
  };

  const handleCommentAdded = (latestComments) => {
    setComments(latestComments);
    setCommentsCount(latestComments.length);
  };

  return (
    <div className="post-container">
      {/* Toast */}
      {toast && (
        <ToastNotification
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}

      <div className="post-header">
        <img src={post.userAvatar} alt="Avatar" className="post-avatar" />
        <div className="post-info">
          <span className="post-user-name">{post.userName}</span>
          <span className="post-time">
            {new Date(post.createdAt).toLocaleString()}
          </span>
        </div>
      </div>

      <div className="post-content">
        <p>{post.content}</p>
        {post.imageUrl && (
          <img src={post.imageUrl} alt="Post media" className="post-image" />
        )}
      </div>

      <div className="post-stats">
        <span>❤️ {likesCount} Thích</span>
        <span>💬 {commentsCount} Bình luận</span>
      </div>

      <PostFooter
        onLike={handleToggleLike}
        onCommentClick={handleToggleComments}
        isLiked={false}
      />

      {showComments && (
        <CommentList
          postId={post.id}
          comments={comments}
          currentUserId={currentUserId}
          isLoggedIn={isLoggedIn} // 🔹 Truyền thông tin đăng nhập để CommentList cũng hiển thị toast
          onCommentAdded={handleCommentAdded}
          showToast={showToast} // 🔹 Truyền hàm showToast xuống CommentList
        />
      )}
    </div>
  );
};

export default Post;

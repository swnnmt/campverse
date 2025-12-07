import React, { useState, useEffect } from "react";
import communityApi from "../api/communityService";
import CommentItem from "./CommentItem";
import "../components/css/CommentList.css";

const CommentList = ({
  postId,
  comments: initialComments,
  currentUserId,
  onCommentAdded,
  isLoggedIn, // 🔹 Thêm prop kiểm tra đăng nhập
}) => {
  const [comments, setComments] = useState(initialComments || []);
  const [newComment, setNewComment] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setComments(initialComments || []);
  }, [initialComments, postId]);

  // 🟢 Thêm bình luận mới
  const handleAddComment = async (e) => {
    e.preventDefault();
    if (!isLoggedIn) return; // 🔹 Ngăn không cho bình luận nếu chưa đăng nhập
    if (!newComment.trim()) return;

    setIsLoading(true);
    try {
      await communityApi.addComment(postId, currentUserId, newComment);
      const res = await communityApi.getCommentsByPost(postId);
      setComments(res.data);
      if (onCommentAdded) onCommentAdded(res.data);
      setNewComment("");
    } catch (error) {
      console.error("Lỗi khi thêm bình luận:", error);
    } finally {
      setIsLoading(false);
    }
  };

  // 🟣 Gửi phản hồi bình luận
  const handleReplySubmit = async (parentCommentId, replyText) => {
    if (!isLoggedIn) return; // 🔹 Ngăn không cho reply nếu chưa đăng nhập
    try {
      await communityApi.replyToComment(
        postId,
        currentUserId,
        replyText,
        parentCommentId
      );
      const res = await communityApi.getCommentsByPost(postId);
      setComments(res.data);
      if (onCommentAdded) onCommentAdded(res.data);
    } catch (error) {
      console.error("Lỗi khi gửi phản hồi:", error);
    }
  };

  return (
    <div className="comment-section">
      <div className="comment-input">
        <form onSubmit={handleAddComment}>
          <input
            type="text"
            placeholder={
              isLoggedIn ? "Viết bình luận..." : "Cần đăng nhập để bình luận"
            }
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            disabled={!isLoggedIn || isLoading} // 🔹 Disabled nếu chưa đăng nhập
          />
          <button type="submit" disabled={!isLoggedIn || isLoading}>
            {isLoading ? "Đang gửi..." : "Gửi"}
          </button>
        </form>
      </div>

      <div className="comment-list">
        {comments.length > 0 ? (
          comments.map((comment) => (
            <CommentItem
              key={comment.id}
              comment={comment}
              onReplySubmit={handleReplySubmit}
              isLoggedIn={isLoggedIn} // 🔹 Truyền để CommentItem cũng kiểm tra đăng nhập
            />
          ))
        ) : (
          <p className="no-comment">Chưa có bình luận nào.</p>
        )}
      </div>
    </div>
  );
};

export default CommentList;

import React, { useState } from "react";
import "../components/css/Comment.css";

/**
 * Hiển thị một bình luận + danh sách phản hồi (có thể ẩn/hiện)
 * @param {object} props
 * @param {object} props.comment
 * @param {function} props.onReplySubmit
 * @param {boolean} props.isLoggedIn
 * @param {function} props.showToast
 */
const CommentItem = ({ comment, onReplySubmit, isLoggedIn, showToast }) => {
  const [showReplyBox, setShowReplyBox] = useState(false);
  const [replyText, setReplyText] = useState("");
  const [showReplies, setShowReplies] = useState(true);

  const timeDisplay = new Date(comment.createdAt).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  });

  const handleReplyToggle = () => {
    if (!isLoggedIn) {
      if (showToast) showToast("⚠️ Bạn cần đăng nhập để trả lời!", "error");
      return;
    }
    setShowReplyBox((prev) => !prev);
    setReplyText("");
  };

  const handleReplyFormSubmit = (e) => {
    e.preventDefault();
    if (!isLoggedIn) {
      if (showToast) showToast("⚠️ Bạn cần đăng nhập để trả lời!", "error");
      return;
    }

    const trimmedReply = replyText.trim();
    if (trimmedReply === "") return;

    if (onReplySubmit) {
      onReplySubmit(comment.id, trimmedReply);
    }

    setReplyText("");
    setShowReplyBox(false);
  };

  return (
    <div className="comment-item">
      <img
        src={comment.userAvatar}
        alt={`${comment.userName}'s avatar`}
        className="comment-avatar"
      />

      <div className="comment-main-content">
        <div className="comment-header">
          <span className="comment-user-name">{comment.userName}</span>
        </div>

        <div className="comment-body-text-with-time">
          <span className="comment-content">{comment.content}</span>
          <span className="comment-time-inline"> • {timeDisplay}</span>
        </div>

        <div className="comment-actions">
          <button className="reply-btn" onClick={handleReplyToggle}>
            {showReplyBox ? "Hủy" : "Trả lời"}
          </button>

          {comment.replies && comment.replies.length > 0 && (
            <button
              className="toggle-replies-btn"
              onClick={() => setShowReplies(!showReplies)}
            >
              {showReplies
                ? `Ẩn ${comment.replies.length} phản hồi`
                : `Hiện ${comment.replies.length} phản hồi`}
            </button>
          )}
        </div>

        {showReplyBox && (
          <form className="reply-form" onSubmit={handleReplyFormSubmit}>
            <input
              type="text"
              placeholder={
                isLoggedIn
                  ? `Phản hồi ${comment.userName}...`
                  : "Cần đăng nhập để trả lời"
              }
              value={replyText}
              onChange={(e) => setReplyText(e.target.value)}
              disabled={!isLoggedIn}
            />
            <button type="submit" disabled={!isLoggedIn || !replyText.trim()}>
              Gửi
            </button>
          </form>
        )}

        {showReplies && comment.replies && comment.replies.length > 0 && (
          <div className="comment-replies">
            {comment.replies.map((reply) => (
              <CommentItem
                key={reply.id}
                comment={reply}
                onReplySubmit={onReplySubmit}
                isLoggedIn={isLoggedIn}
                showToast={showToast}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default CommentItem;

import React, { useState, useEffect } from "react";
import CreatePost from "../components/CreatePost";
import Post from "../components/Post";
import communityApi from "../api/communityService";
import "../components/css/CommunityFeed.css";

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
        } catch (e) {
          if (storedValue.length > 10) return storedValue;
        }
      }
    }
  }
  return userId || null;
};

const CommunityFeed = () => {
  const currentUserId = getUserIdFromLocalStorage();
  const [isLoggedIn] = useState(!!currentUserId);

  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [size] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const fetchPosts = async (pageNumber = 0) => {
    setLoading(true);
    try {
      const response = await communityApi.getPostsPaged(pageNumber, size);
      setPosts(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(pageNumber);
    } catch (error) {
      console.error("Lỗi khi tải bài đăng:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPosts(0);
  }, []);

  return (
    <div className="feed-container" style={{ paddingTop: "150px" }}>
      {isLoggedIn && (
        <CreatePost
          onPostCreated={() => fetchPosts(page)}
          currentUserId={currentUserId}
        />
      )}

      <div className="post-feed">
        {posts.length > 0 ? (
          posts.map((post) => (
            <Post
              key={post.id}
              post={post}
              currentUserId={currentUserId}
              isLoggedIn={isLoggedIn}
            />
          ))
        ) : (
          <p>Chưa có bài đăng nào. Hãy là người đầu tiên chia sẻ!</p>
        )}
      </div>

      <div className="pagination">
        <button onClick={() => fetchPosts(page - 1)} disabled={page === 0}>
          Trang trước
        </button>
        <span>
          {" "}
          {page + 1} / {totalPages}{" "}
        </span>
        <button
          onClick={() => fetchPosts(page + 1)}
          disabled={page + 1 >= totalPages}
        >
          Trang sau
        </button>
      </div>
    </div>
  );
};

export default CommunityFeed;

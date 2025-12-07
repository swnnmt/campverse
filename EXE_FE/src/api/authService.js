import axios from "axios";
import { getErrorMessage } from "../utils/apiError";

const API_BASE_URL = "http://localhost:8080/api/v1/auth";

export const login = async (email, password) => {
  try {
    const res = await axios.post(`${API_BASE_URL}/doLogin`, {
      email,
      password,
    });

    // Nếu backend trả token, lưu vào localStorage
    if (res.data.data.accessToken) {
      localStorage.setItem("token", res.data.data.accessToken);
    }

    return res.data;
  } catch (error) {
    // Normalize error: throw Error with backend message so callers can display it
    const msg = getErrorMessage(error) || "Login failed";
    throw new Error(msg);
  }
};

export const logout = () => {
  localStorage.removeItem("token");
};

export const getProfile = async (token) => {
  try {
    const res = await axios.get(`${API_BASE_URL}/profile`, {
      headers: {
        Authorization: `Bearer ${token}`, // gửi token trong header
      },
    });
    console.log("Profile data:", res.data);
    localStorage.setItem("user", JSON.stringify(res.data.data));
    return res.data;
  } catch (error) {
    if (error.response) {
      console.error("Get profile failed:", error.response.data);
      throw error.response.data;
    } else {
      console.error("Error:", error.message);
      throw error;
    }
  }
};

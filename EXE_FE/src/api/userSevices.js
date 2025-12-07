import axios from "axios";
import { getErrorMessage } from "../utils/apiError";

const API_BASE_URL = "http://localhost:8080/api/v1/user";
export const register = async (
  firstName,
  lastName,
  phoneNumber,
  address,
  department,
  email,
  gender,
  password,
  role
) => {
  try {
    const res = await axios.post(`${API_BASE_URL}/register`, {
      firstName,
      lastName,
      phoneNumber,
      address,
      department,
      email,
      gender,
      password,
      role,
    });

    console.log("Register success:", res.data);
    return res.data;
  } catch (error) {
    const msg = getErrorMessage(error);
    console.error(
      "Register failed:",
      msg,
      error.response?.data ?? error.message
    );
    // throw Error so UI can show message
    throw new Error(msg);
  }
};

export const activeAccount = async (otp) => {
  try {
    const res = await axios.get(`${API_BASE_URL}/activate-account`, {
      params: { validOtp: otp }, // gửi OTP qua query param
    });

    console.log("Active account success:", res.data);
    return res.data;
  } catch (error) {
    const msg = getErrorMessage(error);
    console.error(
      "Active account failed:",
      msg,
      error.response?.data ?? error.message
    );
    throw new Error(msg);
  }
};

export const resendOTP = async (email) => {
  try {
    const res = await axios.post(`${API_BASE_URL}/resend-otp`, { email });
    console.log("Resend OTP success:", res.data);
    return res.data;
  } catch (error) {
    const msg = getErrorMessage(error);
    console.error(
      "Resend OTP failed:",
      msg,
      error.response?.data ?? error.message
    );
    throw new Error(msg);
  }
};

export const updateProfile = async (
  firstName,
  lastName,
  phoneNumber,
  address,
  department,
  gender,
  avatarUrl
) => {
  try {
    const token = localStorage.getItem("token"); // hoặc Redux store nếu bạn đang lưu JWT

    const res = await axios.put(
      `${API_BASE_URL}/updateProfile`, // gợi ý endpoint đặt RESTful
      {
        firstName,
        lastName,
        phoneNumber,
        address,
        department,
        gender,
        avatarUrl,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    console.log("Update profile success:", res.data);
    return res.data;
  } catch (error) {
    const msg = getErrorMessage(error);
    console.error(
      "Update profile failed:",
      msg,
      error.response?.data ?? error.message
    );
    throw new Error(msg);
  }
};

export const forgotPassword = async (email) => {
  try {
    const res = await axios.post(`${API_BASE_URL}/forgot-password`, { email });
    console.log("Forgot password success:", res.data);
    return res.data;
  } catch (error) {
    const msg = getErrorMessage(error);
    console.error(
      "Forgot password failed:",
      msg,
      error.response?.data ?? error.message
    );
    throw new Error(msg);
  }
};

export const verifyForgotOTP = async (email, otp) => {
  try {
    // Gọi API verify-forgot-password
    const res = await axios.post(`${API_BASE_URL}/verify-forgot-password`, {
      email,
      otp,
    });
    return res.data;
  } catch (err) {
    const msg = getErrorMessage(err);
    console.error("Error:", msg, err.response?.data ?? err.message);
    throw new Error(msg);
  }
};

export const changePassword = async (
  oldPassword,
  newPassword,
  confirmPassword
) => {
  try {
    const token = localStorage.getItem("token");

    const res = await axios.post(
      `${API_BASE_URL}/change-password`,
      {
        // ✅ phải dùng đúng tên key theo backend DTO
        currentPassword: oldPassword,
        newPassword,
        confirmPassword,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );

    return res.data;
  } catch (err) {
    const msg = getErrorMessage(err);
    console.error("Error:", msg, err.response?.data ?? err.message);
    throw new Error(msg);
  }
};

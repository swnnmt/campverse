import axios from "axios";
import { getErrorMessage } from "../utils/apiError";

const API_BASE_URL = "http://localhost:8080/api/v1/partner";

export const registerPartner = async (partnerData) => {
  try {
    const res = await axios.post(`${API_BASE_URL}/register`, partnerData, {
      headers: {
        "Content-Type": "application/json",
      },
    });

    console.log("✅ Register partner success:", res.data);
    return res.data;
  } catch (error) {
    const msg = getErrorMessage(error);
    console.error(
      "❌ Register partner failed:",
      msg,
      error.response?.data ?? error.message
    );
    throw new Error(msg);
  }
};

// Helper to extract user-friendly error messages from backend responses
export function getErrorMessage(error) {
  if (!error) return "Unknown error";

  // axios error: error.response.data
  const data = error.response?.data ?? error.data ?? error;

  try {
    if (data && typeof data === "object") {
      // Backend uses { meta: { code, message } }
      if (data.meta && data.meta.message) return data.meta.message;
      // Common patterns
      if (data.message) return data.message;
      // If backend sends { error: '...' }
      if (data.error) return data.error;
    }

    // If it's a string, return it
    if (typeof data === "string") return data;
  } catch (e) {
    // swallow
  }

  // fallback to Error.message
  return error.message || "Server error";
}

export function getErrorCode(error) {
  const data = error.response?.data ?? error.data ?? error;
  try {
    if (data && typeof data === "object" && data.meta && data.meta.code)
      return data.meta.code;
  } catch (e) {}
  return null;
}

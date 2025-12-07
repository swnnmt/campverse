// Simple validation utilities used across forms
export const isNotEmpty = (value) => {
  return (
    value !== undefined && value !== null && String(value).trim().length > 0
  );
};

export const isValidEmail = (email) => {
  if (!isNotEmpty(email)) return false;
  // simple email regex
  const re =
    /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\\.,;:\s@\"]+\.)+[^<>()[\]\\.,;:\s@\"]{2,})$/i;
  return re.test(String(email).toLowerCase());
};

export const isValidPhoneVN = (phone) => {
  if (!isNotEmpty(phone)) return false;
  // Vietnamese phone number requirement per user: 10 characters starting with 0
  const re = /^0\d{9}$/;
  return re.test(String(phone));
};

export const isValidPassword = (password) => {
  if (!isNotEmpty(password)) return false;
  // require at least 6 characters (adjustable)
  return String(password).length >= 6;
};

const storage = {};

storage.get = key => {
  const value = localStorage.getItem(key);
  try {
    return JSON.parse(value);
  } catch (e) {
    return value;
  }
};

storage.put = (key, value) => {
  localStorage.setItem(key, JSON.stringify(value));
};

storage.remove = (key) => {
  localStorage.removeItem(key)
};

export default storage;

import axios from 'axios';
import { store } from "./../store";
import storage from "./localStorage";
import { setUser } from "../store/slices/user/UserSlice";


const httpClient = axios.create({
  baseURL: process.env.REACT_APP_SERVER_BASE_URL,
});

httpClient.interceptors.request.use((config) => {
  const token = storage.get("token");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }

  return config;
});

httpClient.interceptors.response.use(
  (config) => {
    config.data = JSON.parse(JSON.stringify(config.data));
    return config
  },
  (error) => {

    if (error.response.status === 401) {
      storage.remove("token");
      store.dispatch(setUser(null));
    }

    return Promise.reject(error.response.data);
  }
);

export default httpClient;


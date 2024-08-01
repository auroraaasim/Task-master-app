import { createAsyncThunk } from "@reduxjs/toolkit";
import httpClient from "../../../lib/httpClient";

export const authenticateUser = createAsyncThunk(
  "user/auth",
  async ({ email, password }) => {
      let user = null;
      try {
          user = await httpClient.post("/user/authenticate", { email, password });
      } catch (e) {
          console.error(e);
      }
      console.log(user.data);
      return user.data;
  }
);

export const fetchAuthenticatedUser = createAsyncThunk(
  "user/login",
  async () => {
    let user = "";
    try {
      user = await httpClient.get("/user/login");
    } catch (e) {
      console.error(e);
    }
    console.log(user.data);
    return user.data;
  }
);
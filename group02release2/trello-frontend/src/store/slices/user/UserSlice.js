/* Lab code
import { createSlice } from "@reduxjs/toolkit";
import { authenticateUser, fetchAuthenticatedUser } from "./UserThunk";


const initialState = {
  details: {
    data: {},
    isFetching: false,
  },
  authenticate: {
    data: null,
    isFetching: false,
  },
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.details.data = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(authenticateUser.fulfilled, (state, action) => {
      if (
        !action.payload ||
        !action.payload["data"] ||
        !action.payload["data"]["token"]
      ) {
        return;
      }

      state.authenticate.data = action.payload["data"]["token"];
      state.authenticate.isFetching = false;
    });

    builder.addCase(authenticateUser.pending, (state) => {
      state.authenticate.isFetching = true;
    });
  },
});


export const { setUser } = userSlice.actions;

export default userSlice.reducer;
*/


import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  details: {
    data: {},
    isFetching: false,
  },
  authenticate: {
    data: null,
    isFetching: false,
  },
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.authenticate = action.payload;
    },
  },
});

export const { setUser } = userSlice.actions;

export default userSlice.reducer;

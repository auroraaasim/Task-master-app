import { createSlice } from "@reduxjs/toolkit";

const initialState = { data: {} };



const workspaceSlice = createSlice({
    name: "workspace",
    initialState,
    reducers: {
        setWorkspace: (state, action) =>{
            state.data = action.payload;
        }
    },
});

export const {setWorkspace} = workspaceSlice.actions;

export default workspaceSlice.reducer;

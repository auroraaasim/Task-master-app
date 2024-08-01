import { createSlice } from "@reduxjs/toolkit";

const initialState = { data: {member: []}};

const memberSlice = createSlice({
    name: "member",
    initialState,
    reducers: {
        setMember: (state, action) =>{
            state.data.member = [...state.data.member, action.payload]
        }
    },
});

export const {setMember,} = memberSlice.actions;

export default memberSlice.reducer;


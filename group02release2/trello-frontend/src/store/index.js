import { configureStore } from '@reduxjs/toolkit'
import logger from 'redux-logger'
import UserReducer from './slices/user/UserSlice'
import memberReducer from './slices/member/memberSlice'
import workspaceReducer from './slices/workspace/workspaceSlice'




export const store = configureStore({
  reducer: {
    user: UserReducer,
    member: memberReducer,
    workspace: workspaceReducer,
  },
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(logger),
  devTools: process.env.NODE_ENV !== 'production',
})

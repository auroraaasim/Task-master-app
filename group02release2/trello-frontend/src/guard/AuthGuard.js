import React from "react";
import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import AppLayout from "../layouts/AppLayout";

const AuthGuard = ({ component: Component, auth, ...rest }) => {
  const { data: token } = useSelector((state) => state.user.authenticate);
  return token ? <AppLayout /> : <Navigate to="/" />;
};

export default AuthGuard;

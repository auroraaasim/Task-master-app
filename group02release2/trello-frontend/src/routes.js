import { Navigate, useRoutes } from "react-router-dom";
import LogoOnlyLayout from "./layouts/LogoOnlyLayout";
import Login from "./Components/Login";
import Home from "./pages/Home";
import BoardContainer from "./pages/BoardContainer";
import Member from "./pages/Member";
import CreateWorkspace from "./pages/CreateWorkspace";
import AuthGuard from "./guard/AuthGuard";
import Register from "./pages/Register";
import Reset from "./pages/Reset";
import LoginAuth from "./pages/LoginAuth";

export default function Router() {
  return useRoutes([
    {
      path: "/",
      element: <LogoOnlyLayout />,
      children: [
        {
          index: true,
          element: <Navigate to="/register" />,
        },
        {
          path: "login",
          element: <LoginAuth />,
        },
        { path: "register", element: <Register /> },
        { path: "reset",element:<Reset />},
        { path: "home", element: <AuthGuard />,
        children:[{
          index: true,
          element: <Home />
        },
      ],
    },
        { path: "board", element: <BoardContainer /> },
        { path: "member",element: <Member />},
        { path: "createWorkspace", element: <CreateWorkspace />}
      ],
    },
  ]);
}




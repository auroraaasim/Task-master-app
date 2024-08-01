import "./App.css";
import React from 'react';
import { store } from "./store";
import { Provider } from "react-redux";
import { BrowserRouter } from 'react-router-dom';
import Router from "./routes";
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css'

function App() {
  return (
    <>
      <Provider store={store}>
        <ToastContainer />
      <BrowserRouter>
        <Router />
        </BrowserRouter>
      </Provider>
    </>
  );
}

export default App;

import Login from '../Components/Login';
import { useNavigate } from 'react-router-dom';
import React, { useState } from 'react';
import { Provider } from 'react-redux';
import { store } from '../store/index';
import { useDispatch } from 'react-redux';
import { setUser } from '../store/slices/user/UserSlice';
import {  Grid, Box } from "@mui/material";
import { Link } from "react-router-dom";

function LoginAuth() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  async function handleSubmit(values, setSubmitting) {
    const { email, password } = values;

    try {
      const response = await fetch('http://localhost:9014/user/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        dispatch(setUser({ email, password }));
        

        navigate("/board")
      } else {
        // if login failed
        console.log('Error.Login failed');
        
      }
    } catch (error) {
      console.log(error);
    }
    finally {
       setSubmitting(false);
    }
  }

  return (
      <div>
        <Box textAlign="center">
          <h1> Trello </h1>
          <Login onLogin={handleSubmit} />
          <Grid >
            <Link to="/Reset">Forgot Password</Link>
          </Grid>
        </Box>
      </div>
    
  );
}
export default LoginAuth;

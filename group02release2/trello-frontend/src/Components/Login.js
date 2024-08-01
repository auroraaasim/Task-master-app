/*
import * as Yup from "yup";
import { Form, FormikProvider, useFormik } from "formik";
import { LoadingButton } from "@mui/lab";
import { Stack, TextField } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { authenticateUser } from "../store/slices/user/UserThunk";
import { toast } from "react-toastify";
import storage from "../lib/localStorage";
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import axios from 'axios';


export default function Login(onLogin) {
  const LoginSchema = Yup.object().shape({
    email: Yup.string()
      .email("Email must be a valid email address")
      .required("Email is required"),
    password: Yup.string().required("Password is required"),
  });


  const navigate = useNavigate()
  const dispatch = useDispatch()
 
  const formik = useFormik({
    initialValues: {
      email: null,
      password: null,
    },
    validationSchema: LoginSchema,
    onSubmit: async (values, {setSubmitting}) => {
      const { email, password } = values;
      onLogin(values,setSubmitting);
      //console.log(email, password);
      
  /*    dispatch(
        authenticateUser({
          email,
          password,
        })
      )
        .then((response) => {
          const { payload } = response;
  
          if (!payload || !payload["data"]) {
            toast.error("Something went wrong! Try again later");
            return;
          }

          const data = payload["data"]
          if (payload["status"] !== "SUCCESS") {
            toast.error(data["message"]);
            return;
          }
  
          
          storage.put("token", data["token"]);
          navigate("/home");
        })
        .catch((error) => {
          console.log(error);
        });
        
    },
     
  });


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
        dispatch(authenticateUser({ email, password }));
        

        navigate("/board")
      } else {
        // if login failed
        console.log('Login failed');
        
      }
    } catch (error) {
      console.log('Error:', error);
    }
    finally {
       setSubmitting(false);
    }
  }



  const { errors, touched, isSubmitting, handlesubmit, getFieldProps } = formik;

  return (
    <Stack spacing={5}>
      <Box>
            <Typography
              variant="h3"
              sx={{
                textAlign: "center",
              }}
            >
              Trello Clone
            </Typography>
            <Typography
              variant="h4"
              sx={{
                textAlign: "center",
              }}
            >
              Login Here
            </Typography>
          </Box>
      <FormikProvider value={formik}>
        <Form autoComplete="off" noValidate onSubmit={handlesubmit}
        onLogin={handleSubmit}>   
          <Stack spacing={3}>
            <TextField
              fullWidth
              autoComplete="username"
              type="email"
              label="Email address"
              {...getFieldProps("email")}
              error={Boolean(touched.email && errors.email)}
              helperText={touched.email && errors.email}
            />

            <TextField
              fullWidth
              type="password"
              label="Password"
              {...getFieldProps("password")}
              error={Boolean(touched.password && errors.password)}
              helperText={touched.password && errors.password}
            />

            <LoadingButton
              fullWidth
              size="large"
              type="submit"
              variant="contained"
              loading={isSubmitting}
            >
              Login
            </LoadingButton>
          </Stack>
        </Form>
      </FormikProvider>
    </Stack>
  );
}
*/
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import React from 'react';
import { Form, FormikProvider, useFormik } from "formik";
import * as Yup from "yup";
import { LoadingButton } from '@mui/lab';
import { Stack, TextField } from "@mui/material";

function Login({ onLogin }) {
  const LoginSchema = Yup.object().shape({
    email: Yup.string()
      .email("Email must be a valid email address")
      .required("Email is required"),
    password: Yup.string().required("Password is required"),
  });

  const formik = useFormik({
    initialValues: {
      email: null,
      password: null,
    },
    validationSchema: LoginSchema,
    onSubmit: async (values, {setSubmitting}) => {
      const { email, password } = values;
      onLogin(values,setSubmitting);
    }
  });

  const { errors, touched, isSubmitting, handleSubmit, getFieldProps } = formik;

  return (
   
   
    <FormikProvider value={formik}>
    <Form autoComplete="off" noValidate onSubmit={handleSubmit}>   
      <Stack spacing={3}>
        <TextField
          fullWidth
          autoComplete="username"
          type="email"
          label="Email address"
          {...getFieldProps("email")}
          error={Boolean(touched.email && errors.email)}
          helperText={touched.email && errors.email}
        />

        <TextField
          fullWidth
          type="password"
          label="Password"
          {...getFieldProps("password")}
          error={Boolean(touched.password && errors.password)}
          helperText={touched.password && errors.password}
        />

        <LoadingButton
          fullWidth
          size="large"
          type="submit"
          variant="contained"
          loading={isSubmitting}
        >
          Login
        </LoadingButton>
      </Stack>
    </Form>
  </FormikProvider>
  
  );
}

export default Login;

import React, { useState } from "react";
import axios from "axios";
import {
  Box,
  Container,
  IconButton,
  InputAdornment,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { Form, FormikProvider, useFormik } from "formik";
import * as Yup from "yup";
import VisibilityIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { MenuItem } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import storage from "../lib/localStorage";
import { authenticateUser } from "../store/slices/user/UserThunk";
import { setAuthenticate, setUser } from "../store/slices/user/UserSlice";

const Register = () => {
  const [showPassword, setShowPassword] = useState(false);
  const RegisterSchema = Yup.object().shape({
    first_name: Yup.string()
      .min(2, "Too Short!")
      .max(50, "Too Long!")
      .required("First name required"),
    last_name: Yup.string()
      .min(2, "Too Short!")
      .max(50, "Too Long!")
      .required("Last name required"),
    email: Yup.string()
      .email("Email must be a valid email address")
      .required("Email is required"),
    password: Yup.string()
      .required("Password is required")
      .min(8, "Too Short!")
      .matches(
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])/,
        "Password must contain at least one lowercase letter, one uppercase letter, one number, and one symbol"
      ),
    passwordConfirmation: Yup.string().oneOf(
      [Yup.ref("password"), null],

      "Passwords must match"
    ),
    security_question: Yup.string()
    .required("Must select a security question"),

    security_answer: Yup.string().required("securityAnswer is required"),
  });

  const dispatch = useDispatch();

  const navigate = useNavigate();

  const [first_name, setFName] = useState("");

  const [last_name, setLName] = useState("");

  const [username, setUserName] = useState("");

  const [email, setEmail] = useState("");

  const [password, setPassword] = useState("");

  const security_question = [
    { value: "", label: "Select Type" },

    {
      value: "What is your favourite color?",
      label: "What is your favourite color?",
    },

    { value: "What is your nick name?", label: "What is your nick name?" },
    ];

  const [security_answer, setSecurity_answer] = useState("");


  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // Send registration data to backend

      const response = await axios.post("http://localhost:9014/user/register", {
        first_name,

        last_name,
        username,

        email,

        password,

        security_question,

        security_answer,
      });

      // Registration successful

      console.log(response.data);

      navigate("/login");
    } catch (error) {
      // Handle registration error

      console.error(error);
    }
  };

  const formik = useFormik({
    initialValues: {
      first_name: "",

      last_name: "",

      username:"",

      email: "",

      password: "",

      passwordConfirmation: "",

      security_question: "",

      security_answer: "",
    },

    validationSchema: RegisterSchema,

    onSubmit: async (values) => {
      const { first_name, last_name,username, password,email,security_question, security_answer } = values;

      //console.log(values);

      dispatch(
        authenticateUser({
          email,

          password,
        })
      )
        .then((response) => {
          const { payload } = response;

          console.log(payload);

          if (!payload) {
            toast.error("Something went wrong! Try again later");

            return;
          }

          if (payload["status"] !== "SUCCESS") {
            toast.error(payload["message"]);

            return;
          }

          const data = payload["data"];

          storage.put("token", data);

          dispatch(setUser(data));

          navigate("/login");
        })
        .catch((error) => {
          console.log(error);
        });
    },
  });

  const { errors, touched, isSubmitting, handlSubmit, getFieldProps } = formik;

  return (
    <Container maxWidth="sm" sx={{ height: "100%" }}>
      <Box sx={{ mt: 20 }}>
        <Stack spacing={5}>
          <Box>
            <Typography
              variant="h3"
              sx={{
                textAlign: "center",
              }}
            >
              Register
            </Typography>
          </Box>

          <FormikProvider value={formik}>
            <Form autoComplete="off" noValidate onSubmit={handleSubmit}>
              <Stack spacing={3}>
                <TextField
                  fullWidth
                  label="First name"
                  value={first_name}
                  onChange={(e) => setFName(e.target.value)}
                  // {...getFieldProps("firstName")}

                  error={Boolean(touched.first_name && errors.first_name)}
                  helperText={touched.first_name && errors.first_name}
                />

                <TextField
                  fullWidth
                  label="Last name"
                  value={last_name}
                  onChange={(e) => setLName(e.target.value)}
                  // {...getFieldProps("lastName")}

                  error={Boolean(touched.last_name && errors.last_name)}
                  helperText={touched.last_name && errors.last_name}
                />

                <TextField
                  fullWidth
                  label="User Name"
                  value={username}
                  onChange={(e) => setUserName(e.target.value)}
                  // {...getFieldProps("lastName")}

                  error={Boolean(touched.username && errors.username)}
                  helperText={touched.username && errors.username}
                />      

                <TextField
                  fullWidth
                  label="Email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  // {...getFieldProps("email")}

                  error={Boolean(touched.email && errors.email)}
                  helperText={touched.email && errors.email}
                />

                <TextField
                  fullWidth
                  type="password"
                  label="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  //{...getFieldProps("password")}

                  error={Boolean(touched.password && errors.password)}
                  helperText={touched.password && errors.password}
                />

                <TextField
                  fullWidth
                  type={showPassword ? "text" : "password"}
                  label="Confirm Password"
                  // {...getFieldProps("passwordConfirmation")}

                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          edge="end"
                          onClick={() => setShowPassword((prev) => !prev)}
                        >
                          {showPassword ? (
                            <VisibilityIcon />
                          ) : (
                            <VisibilityOffIcon />
                          )}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                  error={Boolean(
                    touched.passwordConfirmation && errors.passwordConfirmation
                  )}
                  helperText={
                    touched.passwordConfirmation && errors.passwordConfirmation
                  }
                />

          <TextField
            fullWidth
            select
            label="Security Question"
            //{...getFieldProps("securityQuestion")}
            error={Boolean(touched.security_question && errors.security_question)}
            helperText={touched.security_question && errors.security_question}
          >
            {security_question.map((question) => (
              <MenuItem key={question.value} value={question.value}>
                {question.label}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            fullWidth
            label="Security Answer"
            value={security_answer}
            onChange={(e) => setSecurity_answer(e.target.value)}
            //{...getFieldProps("security_answer")}
            error={Boolean(touched.security_answer && errors.security_answer)}
            helperText={touched.security_answer && errors.security_answer}
          />

                <LoadingButton
                  loading={isSubmitting}
                  fullWidth
                  variants="contained"
                  type="submit"
                >
                  Submit
                </LoadingButton>
              </Stack>
            </Form>
          </FormikProvider>
        </Stack>

        <p>
          <Link component={Link} to="/reset">
            Reset Password
          </Link>
        </p>

        <p>
          Already registered? <Link to="/login">Log in</Link>
        </p>
      </Box>
    </Container>
  );
};

export default Register;

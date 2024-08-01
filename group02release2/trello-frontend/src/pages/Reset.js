import React, { useState } from "react";
import { Form } from "formik";
import * as Yup from "yup";
import { TextField, Typography } from "@mui/material";
import { FormikProvider, useFormik } from "formik";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { LoadingButton } from "@mui/lab";
import { MenuItem } from "@mui/material";
import { Container } from "@mui/material";

const Reset = () => {
  const securityQuestions = [
    { value: "", label: "Select Type" },

    {
      value: "What is your favourite color?",
      label: "What is your favourite color?",
    },

    { value: "What is your nick name?", label: "What is your nick name?" },
  ];

  const ResetSchema = Yup.object().shape({
    email: Yup.string()

      .email("Email must be a valid email address")

      .required("Email is required"),

    newPassword: Yup.string()

      .required("New password is required")

      .min(8, "New password must be at least 8 characters long")

      .matches(
        /^(?=.*[A-Z])/,
        "Password must contain at least one uppercase letter"
      )

      .matches(
        /^(?=.*[a-z])/,
        "Password must contain at least one lowercase letter"
      )

      .matches(/^(?=.*\d)/, "Password must contain at least one number")

      .matches(
        /^(?=.*[@$!%*?&])/,
        "Password must contain at least one special character"
      ),

    confirmPassword: Yup.string()

      .required("Confirm password is required")

      .oneOf([Yup.ref("newPassword"), null], "Passwords must match"),

    securityQuestion: Yup.string()
    .required("Must select a security question"),

    securityAnswer:
      Yup.string()
      .required("Answer is required"),
  });

  const [resetPasswordSuccess, setResetSuccess] = useState(false);

  const navigate = useNavigate();

  const formik = useFormik({
    initialValues: {
      email: "",

      newPassword: "",

      confirmPassword: "",

      securityQuestion: "",

      securityAnswer: "",
    },

    validationSchema: ResetSchema,

    onSubmit: async (values) => {
      const { email, newResetPassword, securityQuestion, securityAnswer } =
        values;

      try {
        // Make the API request to reset the password

        await axios.post("http://localhost:9014/update-password", {
          email,

          newResetPassword,

          securityQuestion,

          securityAnswer,
        });

        navigate("/login");

        setResetSuccess(true);
      } catch (error) {
        console.log("An error occurred:", error.message);
      }
    },
  });

  const { errors, touched, isSubmitting, handleSubmit, getFieldProps } = formik;

  return (
    <Container
    style={{
      backgroundColor: "#f5f5f5",
      height: "100vh",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "center",
      padding: "2rem",
    }}
  >
    <Typography
      variant="h1"
      style={{
        fontSize: "40px",
        color: "#333",
        marginBottom: "1rem",
        textAlign: "center",
        textShadow: "2px 2px 4px rgba(0, 0, 0, 0.3)",
      }}
    >
      Reset Password
    </Typography>

    {resetPasswordSuccess ? (
      <Typography variant="body1">
        Your password has been reset successfully.
      </Typography>
    ) : (
      <FormikProvider value={formik}>
        <Form
          autoComplete="off"
          noValidate
          onSubmit={handleSubmit}
          style={{ width: "200px" }}
        >
          <TextField
            fullWidth
            label="Email"
            {...getFieldProps("email")}
            error={Boolean(touched.email && errors.email)}
            helperText={touched.email && errors.email}
          />

          <TextField
            fullWidth
            label="New Password"
            type="password"
            {...getFieldProps("newPassword")}
            error={Boolean(touched.newPassword && errors.newPassword)}
            helperText={touched.newPassword && errors.newPassword}
          />

          <TextField
            fullWidth
            label="Confirm Password"
            type="password"
            {...getFieldProps("confirmPassword")}
            error={Boolean(touched.confirmPassword && errors.confirmPassword)}
            helperText={touched.confirmPassword && errors.confirmPassword}
          />

          <TextField
            fullWidth
            select
            label="Security Question"
            {...getFieldProps("securityQuestion")}
            error={Boolean(touched.securityQuestion && errors.securityQuestion)}
            helperText={touched.securityQuestion && errors.securityQuestion}
          >
            {securityQuestions.map((question) => (
              <MenuItem key={question.value} value={question.value}>
                {question.label}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            label="Security Answer"
            fullWidth
            {...getFieldProps("securityAnswer")}
            error={Boolean(touched.securityAnswer && errors.securityAnswer)}
            helperText={touched.securityAnswer && errors.securityAnswer}
          />

          <LoadingButton
            loading={isSubmitting}
            type="submit"
            fullWidth
            variant="contained"
          >
            Confirm
          </LoadingButton>
        </Form>
      </FormikProvider>
    )}
  </Container>
 
  )
  
};

export default Reset;

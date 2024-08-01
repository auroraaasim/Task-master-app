import * as Yup from "yup";
import { useState } from "react";
import { Form, FormikProvider, useFormik } from "formik";
import { LoadingButton } from "@mui/lab";
import {
    Container,
    Stack,
    TextField,
    Box,
    Typography,
} from "@mui/material";

import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { setMember } from "../store/slices/member/memberSlice";
import axios from "axios";

export default function Member() {

    const MemberSchema = Yup.object().shape({
        member: Yup.string().required("Email or name is required"),
    });

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const formik = useFormik({
        initialValues: {
            member: "",
        },
        validationSchema: MemberSchema,
        onSubmit: async (values) => {
            const { member } = values;
            console.log(member);

            try {
                const response = await axios.post("http://localhost:9014/workspace/addMember/{workspaceId}?userId=${member}", {
                    member,
                })

                console.log("Add member: ", response.data);

                dispatch(
                    setMember({
                        member,
                    })
                );

                navigate("/board");
            } catch (error) {
                console.error("Error adding member: ", error);
            }
        },
    });

    const { errors, touched, isSubmitting, handleSubmit, getFieldProps } = formik;

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
                            Add member to workspace
                        </Typography>
                    </Box>
                    <FormikProvider value={formik}>
                        <Form autoComplete="off" noValidate onSubmit={handleSubmit}>
                            <Stack spacing={3}>
                                <TextField
                                    fullWidth
                                    label="Member"
                                    {...getFieldProps("member")}
                                    error={Boolean(touched.member && errors.member)}
                                    helperText={touched.member && errors.member}
                                />



                                <LoadingButton
                                    fullWidth
                                    size="large"
                                    type="submit"
                                    variant="contained"
                                    loading={isSubmitting}
                                >
                                    Send Invitation
                                </LoadingButton>
                            </Stack>
                        </Form>
                    </FormikProvider>
                </Stack>
            </Box>
        </Container>
    );
}



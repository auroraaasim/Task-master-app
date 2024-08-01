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
import { setWorkspace } from "../store/slices/workspace/workspaceSlice";
import axios from "axios";

export default function Member() {
    const [showName, setShowName] = useState(false);



    const WorkspaceSchema = Yup.object().shape({
        name: Yup.string().required("The name of workspace!"),
        type: Yup.string().required("The field you want to use this workspace in!"),
        description: Yup.string().required("Introduce the workspace to your teammate!"),
    });

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const formik = useFormik({
        initialValues: {
            name: "",
            type: "",
            description: "",
        },
        validationSchema: WorkspaceSchema,
        onSubmit: async (values) => {
            const { name, type, description } = values;
            console.log(name, type, description);

            try {
                const response = await axios.post("http://localhost:9013/workspace/save", {
                    name,
                    type,
                    description,
                })


                console.log("Workspace created: ", response.data);

                dispatch(
                    setWorkspace({
                        name,
                        type,
                        description,
                    })
                );

                navigate("/board");
            } catch (error) {
                console.error("Error create workspace: ", error);
            }




            /*
            dispatch(
                setWorkspace({
                    name,
                    type,
                    description
                })
            );

*/
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
                            Build Workspace
                        </Typography>
                    </Box>
                    <FormikProvider value={formik}>
                        <Form autoComplete="off" noValidate onSubmit={handleSubmit}>
                            <Stack spacing={3}>
                                <TextField
                                    fullWidth
                                    label="Workspace name"
                                    {...getFieldProps("name")}
                                    error={Boolean(touched.name && errors.name)}
                                    helperText={touched.name && errors.name}
                                />

                                <TextField
                                    fullWidth
                                    label="Workspace type"
                                    {...getFieldProps("type")}
                                    error={Boolean(touched.type && errors.type)}
                                    helperText={touched.type && errors.type}
                                />

                                <TextField
                                    fullWidth
                                    label="Description"
                                    {...getFieldProps("description")}
                                    error={Boolean(touched.description && errors.description)}
                                    helperText={touched.description && errors.description}
                                />

                                <LoadingButton
                                    fullWidth
                                    size="large"
                                    type="submit"
                                    variant="contained"
                                    loading={isSubmitting}
                                >
                                    Build
                                </LoadingButton>
                            </Stack>
                        </Form>
                    </FormikProvider>
                </Stack>
            </Box>
        </Container>
    );
}

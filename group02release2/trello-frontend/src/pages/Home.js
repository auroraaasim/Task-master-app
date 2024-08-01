import {
  Typography,
  Container,
  Stack,
  Box,
  AppBar,
  Toolbar,
  IconButton,
  Button,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { useSelector } from "react-redux";
import {useNavigate} from "react-router-dom";
import { useDispatch, useEffect } from 'react';
import { fetchAuthenticatedUser } from '../store/slices/user/UserThunk';


export default function Home() {
  const { data: user, isFetching } = useSelector((state) => state.user.data);

  const dispatch = useDispatch();
useEffect(() => {
  dispatch(fetchAuthenticatedUser());
}, [dispatch]);

  const navigate = useNavigate();

  return (
    <Box>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" component="div">
            Trello Clone
          </Typography>
          <Box sx={{ flexGrow: 1, display: "flex", ml: 4 }}>
            
            <Button sx={{ color: "white", fontWeight: "bold" }} onClick={()=> navigate("/member")}>
                          Add Members
            </Button>
            <Button sx={{ color: "white", fontWeight: "bold" }} onClick={()=> navigate("/createWorkspace")}>
              Create Workspace
            </Button>
          </Box>
        </Toolbar>
      </AppBar>

      <Container>
        <Typography variant="h2">Home</Typography>
        {!isFetching && user&&(
          <Stack>
            <Typography>First Name: {user["first_name"]}</Typography>
            <Typography>Last Name: {user["last_name"]}</Typography>
            <Typography>Email: {user["email"]}</Typography>
          </Stack>
        )}
      </Container>
    </Box>
  );
}





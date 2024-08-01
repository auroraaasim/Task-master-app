import React, { useState,useEffect } from "react";
import './BoardContainer.css';
import Board from "../Components/Board.js";
import AddCard from "../Components/AddCard";
import {
    Typography,
    Box,
    AppBar,
    Toolbar,
    IconButton,
    Button,
    Container,
    Stack,
  } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import {useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";

function BoardContainer(){
  /* using useState hook for boards data */
  //const [boardsData, setBoardsData] = useState([]);

  const navigate = useNavigate();
  const member = useSelector((state) => state.member.data);
  const workspace = useSelector((state) => state.workspace.data);
  const [boards,setBoards]=useState([]);
  const [newBoardName, setNewBoardName] = useState('');
  const [searchQuery, setSearchQuery] = useState(''); // New state variable for search query
  const [dueToday, setDueToday] = useState(false);
  const [dueThisWeek, setDueThisWeek] = useState(false);
  const [overdue, setOverdue] = useState(false);
  const [filterOption, setFilterOption] = useState('');


  useEffect(() => {
    fetchBoards();
  }, [dueToday,dueThisWeek,overdue]);




  const fetchBoards = async () => {
    try {
      const response = await fetch('http://localhost:9014/board/getAll', {
        newBoardName,
        dueToday,
        dueThisWeek,
        overdue,
      });
      if (response.ok) {
        const boardsData = await response.json();
        setBoards(boardsData);
      } else {
        console.error('Failed to fetch boards:', response.statusText);
      }
    } catch (error) {
      console.error('Error fetching boards:', error);
    }
  };

  const [target,setTarget]=useState({
    cardId:"",
    boardId: "",
  });

  

 const addCard = async (title, boardId) => {
    const card = {
      id: Math.random() * 100,
      title: "",
      status: [],
      desc: "",
      deadline: "",
    };

    const index = boards.findIndex((item) => item.id === boardId);
    if (index < 0) return;

    const updatedBoards = [...boards];
    const updatedCards = updatedBoards[index].cards || []; // Initialize cards array if it's undefined
    updatedBoards[index].cards = [...updatedCards, card]; // Use spread operator to concatenate the card
  
    setBoards(updatedBoards);
    

    try {
      const response = await fetch('http://localhost:9014/task/save', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          taskName: card.title,
          description: card.desc,
        }),
      });

      if (response.ok) {
        console.log("Create task successfully!");

        const updatedBoards = [...boards];
        const updatedCards = updatedBoards[index].cards || []; // Initialize cards array if it's undefined
        updatedBoards[index].cards = [...updatedCards, card]; // Use spread operator to concatenate the card
        setBoards(updatedBoards);
      } else {
        console.error("Failed to create cards:", response.statusText);
      }
    } catch (error) {
      console.error("Error creating cards: ", error);
    }
  };

   const removeCard = (cardId,boardId)=> {
    const boardIndex = boards.findIndex((item) => item.id === boardId);
    if (boardId < 0) return;

    const cardIndex = boards[boardIndex].cards.findIndex((item) => item.id === cardId);
    if (cardIndex < 0) return;

    const newBoards=[...boards]
    newBoards[boardIndex].cards.splice(cardIndex,1)
    setBoards(newBoards);

  };

  const addBoard = async (title) => {
    const newBoard = {
      id: Math.random() * 100,
      title,
      cards: [],
    };
 
    try {
      const response = await fetch('http://localhost:9014/board/save', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ board_name: newBoardName }),
      });

      if (response.ok) {
        console.log('Board created successfully');
        console.log(newBoard)
        
        setBoards([...boards, newBoard]);
        setNewBoardName('');
      } else {
        console.error('Failed to create board:', response.statusText);
      }
    } catch (error) {
      console.error('Error creating board:', error);
    }
    
  }

  const removeBoard= async (boardId) => {
    const newBoards = boards.filter( (item) => item.id!==boardId)
    setBoards(newBoards);
    
    console.log(boardId)

    //remove board from backend
    try {
      const response = await fetch(`http://localhost:9014/board/delete/${boardId}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error(response.status);
      }
      console.log('Board deleted successfully');
      // Perform any necessary actions after successful deletion
    } catch (error) {
      console.error('Error deleting board:', error.message);
      // Handle the error appropriately
    }
  }
    const handleDragEnd=(cardId,boardId)=>{
    let sourceBIndex,sourceCIndex,targetBIndex,targetCIndex

    sourceBIndex = boards.findIndex(item=>item.id===boardId)
    if(sourceBIndex < 0) return;

    sourceCIndex = boards[sourceBIndex].cards?.findIndex(item=>item.id===cardId)
    if (sourceCIndex < 0) return;

    targetBIndex = boards.findIndex(item=>item.id===target.boardId)
    if (targetBIndex < 0) return;

    targetCIndex = boards[targetBIndex].cards?.findIndex(item=>item.id===target.cardId)
    if (targetCIndex < 0) return;

    const tempBoards = [...boards]
    const tempCards = tempBoards[sourceBIndex].cards[sourceCIndex];

    tempBoards[sourceBIndex].cards.splice(sourceCIndex,1);
    tempBoards[targetBIndex].cards.splice(targetCIndex,0,tempCards);

    setBoards(tempBoards)


  }

  const handleDragEnter=(cardId,boardId)=>{
    setTarget({
      cardId,
      boardId,
    });

  };

  const updateCard = (bid, cid, card) => {
    const index = boards.findIndex((item) => item.id === bid);
    if (index < 0) return;

    const tempBoards = [...boards];
    const cards = tempBoards[index].cards;

    const cardIndex = cards.findIndex((item) => item.id === cid);
    if (cardIndex < 0) return;

    tempBoards[index].cards[cardIndex] = card;

    setBoards(tempBoards);
  };

  const handleSearch = (event) => {
    const query = event.target.value.toLowerCase();
    setSearchQuery(query);
  };

  const filteredBoards = boards.map((item) => {
    // Filter cards based on the search query
    const filteredCards = item.cards
    ? item.cards.filter((card) => {
        // Filter based on search query
        const isTitleMatch = card.title.toLowerCase().includes(searchQuery.toLowerCase());
        const currentDate = new Date();
        const dueDate = new Date(card.deadline);
        const dueToday = dueDate.toDateString() === currentDate.toDateString();
        const nextWeek = new Date();
        nextWeek.setDate(currentDate.getDate() + 7);
        const dueThisWeek = dueDate <= nextWeek && dueDate >= currentDate;
        const overdue = dueDate < currentDate;

         // Apply filter option
         if (filterOption === "dueToday" && !dueToday) {
         return false; // Skip if Due Today option selected but not due today
         }
        if (filterOption === "dueThisWeek" && !(dueThisWeek)) {
        return false; // Skip if Due This Week option selected but not due this week
        }
        if (filterOption === "overdue" && !(overdue)) {
         return false; // Skip if Overdue option selected but not overdue
        }
        
        // Apply search query and due date filters
        if (searchQuery && !isTitleMatch) {
          return false; // Skip if search query is specified but title doesn't match
        }
        if (dueToday && !dueDate.toDateString() === currentDate.toDateString()) {
          return false; // Skip if due today is specified but not due today
        }
        if (dueThisWeek && !(dueDate <= nextWeek && dueDate >= currentDate)) {
          return false; // Skip if due this week is specified but not due this week
        }
        if (overdue && !(dueDate < currentDate)) {
          return false; // Skip if overdue is specified but not overdue
        }
        
        return true; // Return the card if it passes all filters
      })
      : [];
      return { ...item, cards: filteredCards };
      });

      const handleFilterSelection = (option) => {
        setFilterOption(option);
      };

 
  
  return (
    <div className="app">
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
              <Button sx={{ color: "white", fontWeight: "bold" }} onClick={()=> navigate("/login")}>
                LOG OUT
              </Button>
            </Box>
          </Toolbar>
        </AppBar>

        <div>
        <input id="searchBar"
          type="text"
          value={searchQuery}
          onChange={handleSearch}
          placeholder="Search for tasks..."
        />
       </div>

      <div>
      <label>
        
      </label>
      <label>
        <input
          type="radio"
          value="dueToday"
          checked={filterOption === "dueToday"}
          onChange={() => handleFilterSelection("dueToday")}
        />
        Due Today
      </label>
      <label>
        <input
          type="radio"
          value="dueThisWeek"
          checked={filterOption === "dueThisWeek"}
          onChange={() => handleFilterSelection("dueThisWeek")}
        />
        Due This Week
      </label>
      <label>
        <input
          type="radio"
          value="overdue"
          checked={filterOption === "overdue"}
          onChange={() => handleFilterSelection("overdue")}
        />
        Overdue
      </label>
    </div>


        <Container>
            <Stack>
              <Typography>Workspace: {workspace["name"]}</Typography>
              <Typography>Description: {workspace["description"]}</Typography>
              {member["member"].map((m, id)=><Typography key={id}>Member: {m["member"]}</Typography>)}
            </Stack>
        </Container>

      <div className="app_container">
        <div className="app_boards">
          {filteredBoards.map((item) => (
            <Board key={item.id} 
              board={item}  
              removeBoard={removeBoard}
              addCard={addCard}
              removeCard={removeCard}
              handleDragEnd={handleDragEnd}
              handleDragEnter={handleDragEnter}
              updateCard={updateCard}
            />
          ))}
       
          <div className="addNewBoard">
          <AddCard text="Add New Board" placeholder="Board Title"
          onSubmit={(value) => addBoard(value)}></AddCard>
          </div>
          
        </div>
      </div>
      
    </div>
  );
}


export default BoardContainer;

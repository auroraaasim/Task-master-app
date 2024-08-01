import React , {useState}from "react";
import './Board.css';
import MoreHorizSharpIcon from '@mui/icons-material/MoreHorizSharp';
import Card from "./Card";
import AddCard from "./AddCard";
import DeleteBoard from "./DeleteBoard";

//single board contains cards
function Board(props) {
    const [showDeleteBoard,setShow] = useState(false); 
    return (
        <div className="board"> 
           <div className="board_title">
           <p className='board_title_pic' >{props.board?.title}</p>
           <div className="board_top" onClick={()=>setShow(true)}>
             <MoreHorizSharpIcon />
             {
                showDeleteBoard && (
                <DeleteBoard onClose={()=>setShow(false)}>
                <div className="deleteBoard">
                    <p onClick={()=> props.removeBoard(props.board?.id)}>Delete Board</p>
                    </div>
                </DeleteBoard>
                )
             } 
           </div>
          
           </div>

           <div className="board_cards">
            {
                props.board?.cards?.map( (item)=> (
                <Card key = {item.id} card={item}  
                removeCard={props.removeCard}
                boardId = {props.board?.id}
                handleDragEnd={props.handleDragEnd}
                handleDragEnter={props.handleDragEnter}
                updateCard={props.updateCard}
                />))      
            }
            <AddCard 
            onSubmit={(value) => props.addCard(value,props.board?.id)}></AddCard>
           </div>
        </div>
    );
}

export default Board;

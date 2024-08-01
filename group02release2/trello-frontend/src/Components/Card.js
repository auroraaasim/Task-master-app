import React, {useState } from 'react'
import './Card.css'
import Task from './Task'

import { ClearSharp } from '@mui/icons-material'
import CardInfo from './CardInfo';

//single card
function Card(props) { 
  const { id, title, tasks, status } = props.card;
  const [showModal,setShow] = useState(false);
  
    
  return (
    <>
    {showModal && <CardInfo 
    card={props.card}
    updateCard={props.updateCard}
    boardId={props.boardId}
    onClose = {()=> setShow(false)} />}
    <div className='card' draggable
    onDragEnd={()=>props.handleDragEnd(props.card?.id,props.boardId)}
    onDragEnter={()=>props.handleDragEnter(props.card?.id,props.boardId)}
    onClick={()=> setShow(true)}
  
    >
        <div className='card_next'>
            {
                status?.map((item,index)=> (
                  <label key={index} style={{ backgroundColor: item.color }}>
                  {item.text}
                </label>
                )
              )
            }
        </div>
   
      <div className='card_title'>{props.card?.title}
            <ClearSharp onClick={()=> props.removeCard(props.card?.id,props.boardId)}/>

       </div>
       
       
      
      

       
    
        
    </div>
    </>
  )
}

export default Card
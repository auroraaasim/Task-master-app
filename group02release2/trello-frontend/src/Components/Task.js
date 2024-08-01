import React from 'react'
import './Task.css'
import { ClearSharp } from "@mui/icons-material";


function Task(props) {
   
  return (
    <div className='task' style={{backgroundColor: props.color }}>
        <div>
        {props.text}
        {props.close && <ClearSharp onclick={props.onClose ? props.onClose() : " "} />}
        </div>
       
    </div>
  )
}

export default Task; 
import React from "react";
import './CreateBoard.css'

function CreateBoard(props) {

    return (
        <div className="modal" onClick={()=>props.onClose ? props.onClose() : ""}>
            <div className="content" 
            onClick={(event)=>event.stopPropagation()}
            >
                {props.children}
                

            </div>

        </div>
    )

}

export default CreateBoard;


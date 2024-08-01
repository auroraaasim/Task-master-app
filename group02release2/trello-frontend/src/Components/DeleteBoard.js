import React, { useRef , useEffect} from 'react'

function DeleteBoard(props) {
    const deleteRef =useRef()

    const handleClick =(event)=>{
        if(deleteRef && !deleteRef?.current?.contains(event?.target) ) {
            if (props.onClose) props.onClose();
        }
    };
    useEffect(() => {
        document.addEventListener("click",handleClick, { capture: true });
        return ()=>{
            document.removeEventListener("click",handleClick, { capture: true } );
        }
    })

  return (
    <div ref={deleteRef}
    style={{position: "absolute",top: "100%"}} >
        {props.children}
    </div>
  )
}

export default DeleteBoard

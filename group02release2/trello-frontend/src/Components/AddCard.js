import { ClearSharp } from '@mui/icons-material'
import React from 'react'
import { useState } from 'react';
import axios from 'axios';
import './AddCard.css';

function AddCard(props) {
    const [open, setOpen] = useState(false);
    const [input, setInput] = useState(props.text || "");

    const submitTask = async (input) => {
        const taskData = {
                 [props.field] : input
        }

        try {
            const response = await axios.post('http://localhost:9014/task/save', taskData);
            console.log(response.data);
        } catch (error) {
            console.error("Error creating task", error);
        }
    };

    return (
        <div className='addCard'>
            { open ? (
                <form
                    className={`editable ${props.editClass || ""}`}
                    onSubmit={(event)=> {
                        event.preventDefault()
                        if (props.onSubmit) {
                            props.onSubmit(input);
                            submitTask(input);
                        }
                        setOpen(false);
                        setInput("");
                    }}
                >
                    <input
                        type='text'
                        value={input}
                        onChange={(m) => setInput(m.target.value)}
                        placeholder={props.placeholder || "Enter Detail"}
                    />

                    <div className='editable_edit'>
                        <button type='submit'> {props.buttonText || "Add"} </button>
                        <ClearSharp onClick={() => setOpen(false)}/>
                    </div>
                </form>
            ) : (
                <p className={`editable_show ${props.showClass || ""}`} onClick={() => setOpen(true)}>
                    {props.text || "+ Add Card"}
                </p>
            )}
        </div>
    );
}

export default AddCard;

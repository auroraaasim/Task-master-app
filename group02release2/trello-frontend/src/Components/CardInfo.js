import React, {useEffect, useState} from "react";
import CreateBoard from "./CreateBoard";
import './CardInfo.css'
import AddCard from "./AddCard";
import { ClearSharp } from "@mui/icons-material";

// once click single card/task, can edit card/task information: change card/task title, change task status

function CardInfo(props) {

    const [values,setValues] = useState({...props.card})
    const colors = ["lightblue","lightpink","orange"];
    const [selectedColor, setSelectedColor] = useState();
    const [users, setUsers] = useState([]); // State to store fetched users
    const [selectedUser, setSelectedUser] = useState(null); // State to store selected user

    const updateTitle = (value) => {
        setValues({ ...values, title: value });

    };
    const updateDesc = (value) => {
        setValues({ ...values, desc: value });
    };

    const updateDueTime = (newDuedDate) => {
        setValues({...values, dueTime: newDuedDate });
    };



    useEffect(() => {
        if (props.updateCard) props.updateCard(props.boardId, values.id, values);
    }, [values]);

    // Fetch users from the backend on component mount
    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await fetch('http://localhost:9014/user/getAllUser'); // Replace with your backend API endpoint for fetching users
            const data = await response.json();
            setUsers(data); // Assuming the response data is an array of user objects
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    // Function to handle assigning the task to a selected user
    const assignTaskToUser = async () => {
        if (!selectedUser) return;

        try {
            const response = await fetch('http://localhost:9014/task/assign', { // Replace with your backend API endpoint for assigning tasks
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    taskId: values.id,
                    userId: selectedUser.id,
                }),
            });
            const data = await response.json();
            setValues({ ...values, owner: selectedUser.id });
            console.log("Task assigned successfully:", data);
        } catch (error) {
            console.error("Error assigning task:", error);
        }
    };


    const addStatus = (value) => {
        const index = values.status?.findIndex((item) => item.text === value.text);
        if (index > -1) return;
        setSelectedColor("");
        setValues({
            ...values,
            status: [...values.status, value],
        });
    };

    const removeLabel = (value) => {
        const tempStatus = values.status?.filter((item) => item.text !== value.text);

        setValues({
            ...values,
            status: tempStatus,
        });
    };


    return (

        <CreateBoard onClose={()=> props.onClose()}>
            <div className="cardinfo">
                <div className="container">
                    <div className="title">
                        <h3>Add New Task</h3>
                    </div>
                    <div className="task_body">
                        <AddCard text={values.title} defaultValue={values.title} field = {"taskName"}
                                 buttonText="Set Title"
                                 onSubmit={updateTitle}/>
                    </div>

                    <div className="title">
                        <h3>Description</h3>
                    </div>
                    <div className="task_body">
                        <AddCard text={values.desc}

                                 defaultValue={values.desc}

                                 field = {"description"}

                                 onSubmit={updateDesc}/>
                    </div>

                    <div className="deadline">
                        <h3>Due time</h3>
                    </div>
                    <div className= "task_body">
                        <AddCard 
                        placeholder="yyyy-mm-dd hh:mm:ss"
                        text={values.dueTime} defaultValue={values.dueTime} field = {"dueTime"} onSubmit={updateDueTime}/>
                    </div>

                    <div className="title">
                        <h3>Status</h3>
                    </div>

                    <div className="status">
                        {values.status?.map((item, index) => (
                            <label
                                key={index}
                                style={{ backgroundColor: item.color, color: "white" }}
                            >
                                {item.text}
                                <ClearSharp onClick={() => removeLabel(item)} />
                            </label>
                        ))}
                    </div>
                    <ul>
                        {colors.map((item, index) => (
                            <li
                                key={index + item}
                                style={{ backgroundColor: item }}
                                className={selectedColor === item ? "li_active" : ""}
                                onClick={() => setSelectedColor(item)}
                            />
                        ))}
                    </ul>
                    <AddCard 
                             placeholder="Add status:  0: ToDo   1: Doing   2: Done"
                             field = {"status"}
                             onSubmit={(value) =>
                                 addStatus({ color: selectedColor, text: value })
                             }
                    />
                </div>

                <div className="title">
                    <h3>Assign Task to Member</h3>
                </div>
                <div className="task_body">
                    <select value={selectedUser} onChange={(e) => setSelectedUser(e.target.value)}>
                        <option value="">Select a Member</option>
                        {users.map((user) => (
                        <option key={user.id} value={user.id}>
                            {user.email}
                        </option>
                        ))}
                    </select>
                    <button onClick={assignTaskToUser}>Assign</button>
                </div>
            </div>

        </CreateBoard>


    )
}
export default CardInfo;

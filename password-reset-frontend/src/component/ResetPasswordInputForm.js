import React from 'react';
import {useParams} from "react-router-dom";
import axios from "axios";

export default function ResetPasswordInputForm() {
    let {passwordResetHash} = useParams();
    const state = new URLSearchParams(window.location.search).get("state");
    const [password, setPassword] = React.useState("");
    const [status, setStatus] = React.useState("");

    if (!state) {
        return <h1>Invalid request</h1>
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        axios.post("http://localhost:8080/api/v1/reset-password", new URLSearchParams({
            password: data.get('confirmPassword'),
            hash: passwordResetHash
        }), {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }).then((success) => {
            setStatus("Password Reset success");
            window.location.href="https://<auth0_domain>/continue?state="+state;
        }).catch((error) => {
            if(error.status===201) {
                setStatus("Password Reset success");
                window.location.href="https://<auth0_domain>/continue?state="+state;
            }
                console.log(error);
                setStatus("Error while reset password")
            }
        );
    }

    let validatePassword = (event) => {
        if (event.target.value !== password) {
            event.target.setCustomValidity("Passwords did not match");
        } else {
            event.target.setCustomValidity('');
        }
    }

    return (
        <div>
            <h1>Change Password</h1>
            <form onSubmit={handleSubmit}>
                <label>Password</label>
                <input type="password"
                       name="password" id="password"
                       autoFocus
                       required
                       onChange={e => {
                           setPassword(e.target.value)
                       }}/>
                <br/>
                <br/>
                <label>Confirm Password</label>
                <input
                    type="password"
                    name="confirmPassword"
                    id="confirmPassword"
                    required
                    onChange={e => validatePassword(e)}/>
                <br/>
                <br/>
                <button type="submit">Save</button>
            </form>
            <h2>{status}</h2>
        </div>
    )
}
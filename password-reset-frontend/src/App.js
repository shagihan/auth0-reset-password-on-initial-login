import './App.css';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import ResetPasswordInputForm from "./component/ResetPasswordInputForm";

function App() {

    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/reset-password/:passwordResetHash" element={<ResetPasswordInputForm/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;

import {
  BrowserRouter,
  Routes,
  Route
} from "react-router-dom";
import Index from './pages/Index';
import Login from './pages/Login';
import Register from './pages/Register';
import RegisterSuccess from './pages/RegisterSuccess';
import Introduce from './pages/Introduce';
import Reset from './pages/Reset';
import Activate from './pages/Activate';
import Home from './pages/Home';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Index />}/>
        <Route path="/index" element={<Index />}/>
        <Route path="/login" element={<Login />}/>
        <Route path="/register" element={<Register />} />
        <Route path="/register_success" element={<RegisterSuccess />} />
        <Route path="/introduce" element={<Introduce />} />
        <Route path="/reset" element={<Reset />} />
        <Route path="/activate/:email/:checkCode" element={<Activate />} />
        <Route path="/home/:username" element={<Home />}/>
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

const NotFound = () => {
  return <div>你来到了没有知识的荒原</div>
}

export default App;

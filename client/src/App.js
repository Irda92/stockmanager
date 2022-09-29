import React from 'react'
import ProductsStock from './components/ProductsStock'
import UpdateProductStockData from './components/UpdateProductStockData'
import './App.css';

function App() {
  return (
    <div className="container-fluid">
        <nav>
            <div className="nav-wrapper center-align">
                <a href="/" className="brand-logo">Products</a>
            </div>
        </nav>
        <div className="row">
          <UpdateProductStockData />
          <ProductsStock />
        </div>
    </div>
  );
}

export default App;

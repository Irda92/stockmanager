import React, {Component} from 'react';
import SingleProductStock from './SingleProductStock';

export default class ProductsStock extends Component {
    constructor(props) {
        super(props);
        this.state = {
            productStocks: []
        };
    }

    componentDidMount(){
        fetch('http://localhost:8080/testing/product/stock')
        .then(response => response.json())
        .then(data => this.setState({productStocks: data}))
    }

    render() {
        return (
            <div>
                <div className="row">
                    {this.state.productStocks.map((item) => (
                        <SingleProductStock key={item.id} item={item} />
                    ))}
                </div>
            </div>
        )
    }
}
import React, {Component} from 'react';

export default class UpdateProductStockData extends Component {
    constructor() {
        super();
        this.state = {
            itemNumber: "",
            maxStock: "",
            minStock: "",
            lastPurchaseCost: "",
            lastPurchaseDate: "",
            attribute: "",
            }
    }
    submitProductStockData(event) {
        event.preventDefault();

        let productStock = {
         itemNumber: this.itemNumber.value,
                maxStock: this.maxStock.value,
                minStock: this.minStock.value,
                lastPurchaseCost: this.lastPurchaseCost.value,
                lastPurchaseDate: this.lastPurchaseDate.value,
                attribute: this.attribute.value
        }
//        this.setState({itemNumber: this.itemNumber.value});
//        this.setState({maxStock: this.maxStock.value});
//        this.setState({minStock: this.minStock.value});
//        this.setState({lastPurchaseCost: this.lastPurchaseCost.value});
//        this.setState({lastPurchaseDate: this.lastPurchaseDate.value});
//        this.setState({attribute: this.attribute.value});

        fetch("http://localhost:8080/testing/update/product/stock", {
            method: "POST",
            headers: {
                "content-type": "application/json"
            },
            body: JSON.stringify(productStock)
        })
        .then(response => response.json());
        window.location.reload();
    }
    render() {
        return (
             <div className="row">
                <form className="col s12" onSubmit={this.submitProductStockData.bind(this)}>
                  <div className="row">
                    <div className="input-field col s12">
                      <input ref={(call_back) => {this.itemNumber = call_back}} type="text" className="validate"/>
                      <label htmlFor="itemNumber">Cikkszám</label>
                    </div>
                  </div>
                  <div className="row">
                    <div className="input-field col s6">
                      <input ref={(call_back) => {this.minStock = call_back}} type="text" className="validate"/>
                      <label htmlFor="minStock">Mennyi legyen raktáron minimum</label>
                    </div>
                    <div className="input-field col s6">
                      <input ref={(call_back) => {this.maxStock = call_back}} type="text" className="validate"/>
                      <label htmlFor="maxStock">Mennyi legyen raktáron maximum</label>
                    </div>
                  </div>
                  <div className="row">
                    <div className="input-field col s4">
                      $:
                      <div className="input-field inline">
                        <input ref={(call_back) => {this.lastPurchaseCost = call_back}} type="text" className="validate"/>
                        <label htmlFor="lastPurchaseCost">Vételár (termék ár + szállítás)</label>
                      </div>
                    </div>
                    <div className="input-field col s4">
                      <input ref={(call_back) => {this.lastPurchaseDate = call_back}} type="date" className="datepicker"/>
                      <label htmlFor="lastPurchaseDate">Dátum</label>
                    </div>
                    <div className="input-field col s4">
                      <input ref={(call_back) => {this.attribute = call_back}} type="text" className="validate"/>
                      <label htmlFor="attribute">Jellemző</label>
                    </div>
                  </div>
                  <div className="row">
                    <button className="waves-effect waves-light btn" type="submit" name="action">Frissít</button>
                  </div>
                </form>
              </div>
        )
    }
}
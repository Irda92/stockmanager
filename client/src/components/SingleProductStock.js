import React from 'react';

const SingleProductStock = ({item}) => (
     <div className="row">
        <div className="col s12 m6">
          <div className="card blue-grey darken-1">
            <div className="card-content white-text">
              <span className="card-title">{item.itemNumber} {item.attribute}</span>
              <p>{item.actualPrice}</p>
              <p>Min. készlet: {item.minStock} | Max. készlet: {item.maxStock}</p>
              <p>Eladási ár: {item.actualPrice}</p>
              <p>Vételár: {item.lastPurchaseCost} - {item.lastPurchaseDate}</p>
            </div>
          </div>
        </div>
      </div>
)

export default SingleProductStock
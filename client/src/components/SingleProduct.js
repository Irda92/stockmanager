import React from 'react';

const SingleProduct = ({item}) => (
     <div className="row">
        <div className="col s12 m6">
          <div className="card blue-grey darken-1">
            <div className="card-content white-text">
              <span className="card-title">{item.itemNumber} {item.hungarianName}</span>
              <p>{item.englishName}</p>
              <p>Raktáron: {item.quantity}</p>
              <p>Haszon: {item.actualProfit}</p>
            </div>
            <div className="card-action">
              <p>{item.sellingInformation}</p>
              <p>{item.purchaseInformation}</p>
            </div>
          </div>
        </div>
      </div>
)

export default SingleProduct
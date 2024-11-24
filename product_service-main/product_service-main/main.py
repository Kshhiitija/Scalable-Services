from fastapi import FastAPI
import pandas as pd
from pydantic import BaseModel
import os
# from products import add_product, get_product, get_product_by_id
import uvicorn

app = FastAPI()

path = os.getcwd() + '\product_service\product_data.csv'


@app.get("/get-product")
def get_product(product_name : str, all='False'):
    df = pd.read_csv(path)
    if all == 'True':
        prod_data = df
        return prod_data.to_dict('records')
    elif all == 'False':
        prod_data = df.loc[df['product_name'].str.upper() == str.upper(product_name)]
        return prod_data.to_dict('records')

@app.get("/get-product-by-id")
def get_product_by_id(product_id : int):
    df = pd.read_csv(path)
    prod_data = df.loc[df['product_id'] == product_id]
    return prod_data.to_dict('records')

class product_add(BaseModel):
    product_name : str
    product_desc : str

@app.post("/add-product/")
def add_product(product: product_add):
    product_name = product.product_name
    product_desc = product.product_desc
    df = pd.read_csv(path)
    max_key = df['product_id'].max()
    product_id = max_key+1
    with open(path, 'a') as file:
        file.write(f'\n{product_id},{product_name},{product_desc}')

    return get_product_by_id(product_id)

@app.post('/del-product')
def delete_product(product_id: int):
    df = pd.read_csv(path)
    product_info = df[df['product_id'] == product_id].to_dict('records')
    df = df.set_index('product_id')
    df = df.drop([product_id])
    os.remove(path)
    df.to_csv(path)
    return f'Deleted {product_info}'

class product_updt(BaseModel):
    product_id : int
    product_name : str
    product_desc : str

@app.post('/updt-product')
def updt_product(product: product_updt):
    product_id = product.product_id
    product_name = product.product_name
    product_desc = product.product_desc

    df = pd.read_csv(path)
    product_update = {
        'product_id': product_id,
        'product_name': product_name,
        'product_desc': product_desc
    }

    df = df.drop(df[df['product_id'] == 2].index)
    df = df.append(product_update, ignore_index = True)
    df = df.sort_values(by='product_id')
    os.remove(path)
    df.to_csv(path, index=False)

    return f'Updated Product: {get_product_by_id(product_id)}'

if __name__ == "__main__":
    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True, app_dir=os.getcwd(), )


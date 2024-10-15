from fastapi import FastAPI
from strawberry.fastapi import GraphQLRouter
from graphql_schema import Query, Mutation
import strawberry

# Create FastAPI instance
app = FastAPI()

# Create GraphQL schema
schema = strawberry.Schema(query=Query, mutation=Mutation)

# Add GraphQL route
graphql_app = GraphQLRouter(schema)

app.include_router(graphql_app, prefix="/graphql")

# if __name__ == "__main__":
#     import uvicorn
#     uvicorn.run(app, host="0.0.0.0", port=8000)

# Build stage
FROM node:20-alpine as build

WORKDIR /app

# Copy package files
COPY . .
RUN rm -rf node_modules
# Install dependencies
RUN npm install

# Build the application
RUN npm run build

# Production stage
FROM nginx:latest

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built files from build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

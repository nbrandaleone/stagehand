# Deploy Cloud Run with sidecar/init container
gcloud run services replace service.yaml

## Normal deployment command
gcloud run deploy SERVICE --image IMAGE_URL

# Examine running settings
gcloud run services describe SERVICE
gcloud run services describe SERVICE --format export > service.yaml

# Change minimum instance count.  Update service file.
gcloud run services update SERVICE --min-instances MIN-VALUE

# Change to always on model
gcloud run services update SERVICE --no-cpu-throttling

# Update memory. Update service file due to shared volume
## Can use G, M, Gi, Mi
gcloud run services update SERVICE --memory SIZE

#!/bin/bash
set -e

K8S_DIR="./k8s"

# Colors for visibility
YELLOW='\033[1;33m'
GREEN='\033[1;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}🚀 Checking Minikube status...${NC}"
if ! minikube status | grep -q "host: Running"; then
  echo "🔧 Starting Minikube..."
  minikube start
else
  echo "✅ Minikube is already running."
fi

echo -e "${YELLOW}🐳 Switching to Minikube Docker environment...${NC}"
eval $(minikube docker-env)

echo -e "${YELLOW}🏗️  Building Spring Boot Docker image inside Minikube...${NC}"
docker build -t springboot-app:latest .
echo -e "${GREEN}✅ Docker image built successfully!${NC}"

# Apply manifests
if [ -d "$K8S_DIR" ]; then
  echo -e "${YELLOW}📦 Applying all YAML files in ${K8S_DIR}/ ...${NC}"
  find "$K8S_DIR" -type f \( -name "*.yaml" -o -name "*.yml" \) -print
  kubectl apply -f "$K8S_DIR" --recursive
else
  echo -e "${RED}⚠️  Kubernetes directory '$K8S_DIR' not found.${NC}"
  exit 1
fi

# Function to wait for a pod by label
wait_for_pod() {
  local LABEL=$1
  local TIMEOUT=$2
  echo -e "${YELLOW}⏳ Waiting for pod with label $LABEL to be ready...${NC}"

  local SECONDS=0
  while true; do
    POD=$(kubectl get pods -l "$LABEL" -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || true)
    if [[ -z "$POD" ]]; then
      echo "⚠️  No pod found with label $LABEL yet..."
    else
      READY=$(kubectl get pod "$POD" -o jsonpath='{.status.containerStatuses[0].ready}' 2>/dev/null || echo "false")
      STATUS=$(kubectl get pod "$POD" -o jsonpath='{.status.phase}' 2>/dev/null)
      echo "→ Pod: $POD | Status: $STATUS | Ready: $READY"
      if [[ "$READY" == "true" ]]; then
        echo -e "${GREEN}✅ Pod with label $LABEL is ready!${NC}"
        break
      fi
    fi

    if (( SECONDS >= TIMEOUT )); then
      echo -e "${RED}⚠️  Timeout waiting for pod with label $LABEL${NC}"
      kubectl get pods -l "$LABEL"
      break
    fi

    sleep 5
  done
}

# Wait for dependencies
wait_for_pod "app=mysql" 120
wait_for_pod "app=redis" 60
wait_for_pod "app=springboot-app" 180

# Stream logs
#SPRINGBOOT_POD=$(kubectl get pods -l app=springboot-app -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || true)
#if [[ -n "$SPRINGBOOT_POD" ]]; then
#  echo -e "${YELLOW}📜 Streaming logs from Spring Boot pod: $SPRINGBOOT_POD${NC}"
#  kubectl logs -f "$SPRINGBOOT_POD" &
#else
#  echo -e "${RED}⚠️  No running Spring Boot pod found for log streaming.${NC}"
#fi

# Wait for Minikube service URL
echo -e "${YELLOW}🌐 Waiting for Spring Boot service URL...${NC}"
URL=""
SECONDS=0
TIMEOUT=60  # 60 seconds max
while [[ -z "$URL" && $SECONDS -lt $TIMEOUT ]]; do
  URL=$(minikube service springboot-service --url 2>/dev/null || true)
  sleep 2
done

if [[ -n "$URL" ]]; then
  echo -e "${GREEN}🌐 Opening Swagger UI at: $URL${NC}"
  case "$OSTYPE" in
    darwin*) open "$URL" ;;
    linux*) xdg-open "$URL" ;;
    msys*|cygwin*) start "$URL" ;;
    *) echo "Please open manually: $URL" ;;
  esac
else
  echo -e "${RED}⚠️  Could not determine service URL. Check 'springboot-service'.${NC}"
fi

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
        <title>Shipping Order Form</title>
        <link rel="stylesheet" href="css/styles.css">
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    </head>

    <body>
        <div class="container">
            <div class="row justify-content-center text-center mt-5 mb-4">
                <div class="col-12">
                    <h1>Shipping Order Form</h1>
                </div>
            </div>

            <div class="row justify-content-center">
                <div class="col-12 col-md-8 col-lg-6">
                    <div class="card shadow-sm">
                        <div class="card-body p-4">

                            <form>

                                <div class="row mb-3">
                                    <div class="col-6">
                                        <label for="fName" class="form-label">First Name</label>
                                        <input type="text" class="form-control" id="" placeholder="John">
                                    </div>
                                    <div class="col-6">
                                        <label for="customerName" class="form-label">Laast Name</label>
                                        <input type="text" class="form-control" id="" placeholder="Doe">
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email" placeholder="john@example.com">
                                </div>

                                <div class="mb-3">
                                    <label for="product" class="form-label">Product Type</label>
                                    <select class="form-select" id="product">
                                        <option selected disabled>Choose a product...</option>
                                        <option value="1">Product A</option>
                                        <option value="2">Product B</option>
                                        <option value="3">Product C</option>
                                    </select>
                                </div>

                                <div class="row mb-3">
                                    <div class="col-6">
                                        <label for="quantity" class="form-label">Weight (kg)</label>
                                        <input type="number" class="form-control" id="quantity" min="1" value="1">
                                    </div>
                                    <div class="col-6">
                                        <label for="deliveryDate" class="form-label">Area (sq ft) *optional</label>
                                        <input type="number" class="form-control" id="quantity" min="0" value="1">
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="notes" class="form-label">Notes</label>
                                    <textarea class="form-control" id="notes" rows="3"
                                        placeholder="Any special instructions..."></textarea>
                                </div>

                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">Submit Order</button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="javaScript/script.js"></script>
        <script src="bootstrap/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>
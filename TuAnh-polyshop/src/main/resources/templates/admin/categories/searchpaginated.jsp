<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
	<title>Title</title>
	<!-- Required meta tags -->
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />

	<!-- Bootstrap CSS -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
		integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.1/css/all.css"
		integrity="sha384-O8whS3fhG2OnA5Kas0Y9l3cfpmYjapjI0E4theH4iuMD+pLhbf6JI0jIMfYcK3yZ" crossorigin="anonymous" />

	<link rel="stylesheet" href="css/admin.css" />
</head>

<body>
	<main class="container">
		<header class="row"></header>
		<section class="row">
			<div class="col mt-4">
				<div class="card">
					<div class="card-header">
						<h2>Danh sách các sản phẩm</h2>
					</div>
					<div class="card-body">
						<div th:if="${message !=null}" class="alert alert-primary" role="alert">
							<i>[[${message}]]</i>
						</div>
						<div class="row mt-2 mb-2">
							<div class="col">
								<form th:action="@{/admin/categories/searchpaginated}">
									<div class="form-inline float-left">
										<label for="name">Name:</label>
										<input type="text" class="form-control ml-2" name="name" id="name"
											aria-describedby="helpId" placeholder="Name" />
										<button class="btn btn-outline-primary ml-2">
											Searh
										</button>
									</div>
								</form>
								<div class="float-right">
									<a class="btn btn-outline-primary" th:href="@{/admin/categories/add}">Thêm mới
										category</a>
								</div>
							</div>
						</div>
					
						<div class="row" th:if="${!categoryPage.hasContent()}">
							<div class="col">
								<div class="alert alert-danger" role="alert">
									<strong>No Category</strong>
								</div>
							</div>
						</div>
						<table class="table table-striped table-inverse" th:if="${categoryPage.hasContent()}">
							<thead class="thead-inverse">
								<tr>
									<th>Category ID</th>
									<th>Name</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<tr th:each="category,isStat :${categoryPage.content}">
									<td scope="row">[[${category.categoryId}]]</td>
									<td th:text="${category.name}"></td>
									<td class="float-right">
										<a th:href="@{'/admin/categories/view' + ${category.categoryId}}"
											class="btn btn-outline-info"><i class="fas fa-info mr-2"></i>Chi Tiết</a>
										<a th:href="@{'/admin/categories/edit/' + ${category.categoryId}}"
											class="btn btn-outline-warning"><i class="fas fa-edit mr-2"></i>Chỉnh
											sửa</a>
										<a th:href="@{'/admin/categories/delete/' + ${category.categoryId}}"
											class="btn btn-outline-danger"
											onclick="return confirm('bạn thực sự muốn xóa chứ');">Xóa</a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="card-footer text-muted">
						<nav aria-label="Page navigation" 
						th:if="${categoryPage.totalPages > 0}">
							<ul class="pagination justify-content-center">
								<li th:class="${1 == categoryPage.number +1}? 'page-item active' :'page-item'">
									<a th:href="@{/admin/categories/searchpaginated(name=${name},size =${categoryPage.size},page=${1})}"
										class="page-link">First
									</a>
								</li>
								<li class="page-item active" 
								th:each="pageNumber :${pageNumbers}"
								th:if="${categoryPage.totalPages > 1}"
								th:class="${pageNumber == categoryPage.number +1} ? 'page-item active' : 'page-item'">
									<a th:href="@{/admin/categories/searchpaginated(name=${name},size=${categoryPage.size},page=${pageNumber})}"
										class="page-link" th:text="${pageNumber}"></a>
								</li>
								<li
									th:class="${categoryPage.totalPages == categoryPage.number +1}? 'page-item active' :'page-item' ">
									<a th:href="@{/admin/categories/searchpaginated(name=${name},size =${categoryPage.size},page=${categoryPage.totalPages})}"
										class="page-link">Last
									</a>
								</li>
							</ul>
						</nav>
					</div>
				</div>
			</div>
		</section>
		<footer class="row"></footer>
	</main>

	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous"></script>
</body>

</html>
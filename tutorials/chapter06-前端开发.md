# 第六章：前端开发

## 🎯 学习目标
- 掌握现代Web前端开发技术
- 学习HTML5、CSS3和JavaScript ES6+
- 实现响应式用户界面设计
- 掌握AJAX与后端API交互
- 了解前端组件化开发思想

## ⏱️ 预计用时：60分钟

---

## 6.1 前端技术栈介绍

### 📚 理论知识

#### 现代前端技术栈

| 技术 | 作用 | 特点 |
|------|------|------|
| HTML5 | 页面结构 | 语义化标签、表单验证 |
| CSS3 | 样式设计 | Flexbox、Grid、动画 |
| JavaScript ES6+ | 交互逻辑 | 模块化、异步编程 |
| Bootstrap | UI框架 | 响应式、组件丰富 |
| Axios | HTTP客户端 | Promise、拦截器 |

#### 前端架构设计
```
前端架构
├── 页面层（HTML）
│   ├── 学生列表页面
│   ├── 学生详情页面
│   └── 学生编辑页面
├── 样式层（CSS）
│   ├── 基础样式
│   ├── 组件样式
│   └── 响应式样式
├── 逻辑层（JavaScript）
│   ├── API服务
│   ├── 数据管理
│   └── 事件处理
└── 资源层（Assets）
    ├── 图片资源
    ├── 字体文件
    └── 图标库
```

#### 响应式设计原则
1. **移动优先**：从小屏幕开始设计
2. **弹性布局**：使用相对单位
3. **媒体查询**：适配不同设备
4. **触摸友好**：考虑触摸操作

---

## 6.2 页面结构设计

### 🎯 实战任务1：创建主页面HTML

```html
<!-- src/main/resources/static/index.html -->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="学生管理系统 - 高效管理学生信息">
    <meta name="keywords" content="学生管理,教育管理,信息系统">
    <title>学生管理系统</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome 图标 -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- 自定义样式 -->
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-graduation-cap me-2"></i>
                学生管理系统
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="#" data-section="dashboard">
                            <i class="fas fa-tachometer-alt me-1"></i>仪表盘
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-section="students">
                            <i class="fas fa-users me-1"></i>学生管理
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-section="statistics">
                            <i class="fas fa-chart-bar me-1"></i>统计分析
                        </a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-1"></i>管理员
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>设置</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt me-2"></i>退出</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- 主要内容区域 -->
    <main class="container-fluid py-4">
        <!-- 仪表盘部分 -->
        <section id="dashboard-section" class="content-section">
            <div class="row">
                <div class="col-12">
                    <h2 class="mb-4">
                        <i class="fas fa-tachometer-alt me-2"></i>仪表盘
                    </h2>
                </div>
            </div>
            
            <!-- 统计卡片 -->
            <div class="row mb-4">
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left-primary shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                        学生总数
                                    </div>
                                    <div class="h5 mb-0 font-weight-bold text-gray-800" id="total-students">0</div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-users fa-2x text-gray-300"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left-success shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                        专业数量
                                    </div>
                                    <div class="h5 mb-0 font-weight-bold text-gray-800" id="total-majors">0</div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-book fa-2x text-gray-300"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left-info shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                        平均年龄
                                    </div>
                                    <div class="h5 mb-0 font-weight-bold text-gray-800" id="average-age">0</div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-calendar fa-2x text-gray-300"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left-warning shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row no-gutters align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                        今日新增
                                    </div>
                                    <div class="h5 mb-0 font-weight-bold text-gray-800" id="today-new">0</div>
                                </div>
                                <div class="col-auto">
                                    <i class="fas fa-plus fa-2x text-gray-300"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 图表区域 -->
            <div class="row">
                <div class="col-xl-8 col-lg-7">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">专业分布统计</h6>
                        </div>
                        <div class="card-body">
                            <canvas id="majorChart" width="400" height="200"></canvas>
                        </div>
                    </div>
                </div>
                
                <div class="col-xl-4 col-lg-5">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">性别分布</h6>
                        </div>
                        <div class="card-body">
                            <canvas id="genderChart" width="400" height="200"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- 学生管理部分 -->
        <section id="students-section" class="content-section d-none">
            <div class="row">
                <div class="col-12">
                    <h2 class="mb-4">
                        <i class="fas fa-users me-2"></i>学生管理
                    </h2>
                </div>
            </div>
            
            <!-- 操作工具栏 -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#studentModal" onclick="openAddModal()">
                        <i class="fas fa-plus me-1"></i>添加学生
                    </button>
                    <button type="button" class="btn btn-danger ms-2" onclick="deleteSelectedStudents()" disabled id="delete-selected-btn">
                        <i class="fas fa-trash me-1"></i>批量删除
                    </button>
                </div>
                <div class="col-md-6">
                    <div class="input-group">
                        <input type="text" class="form-control" placeholder="搜索学生姓名、学号或专业..." id="search-input">
                        <button class="btn btn-outline-secondary" type="button" onclick="searchStudents()">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </div>
            </div>
            
            <!-- 筛选器 -->
            <div class="row mb-3">
                <div class="col-md-3">
                    <select class="form-select" id="major-filter">
                        <option value="">所有专业</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="gender-filter">
                        <option value="">所有性别</option>
                        <option value="男">男</option>
                        <option value="女">女</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="sort-field">
                        <option value="id">按ID排序</option>
                        <option value="name">按姓名排序</option>
                        <option value="studentNumber">按学号排序</option>
                        <option value="age">按年龄排序</option>
                        <option value="enrollmentDate">按入学日期排序</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="sort-direction">
                        <option value="asc">升序</option>
                        <option value="desc">降序</option>
                    </select>
                </div>
            </div>
            
            <!-- 学生表格 -->
            <div class="card shadow">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>
                                        <input type="checkbox" id="select-all-checkbox" onchange="toggleSelectAll()">
                                    </th>
                                    <th>ID</th>
                                    <th>姓名</th>
                                    <th>学号</th>
                                    <th>年龄</th>
                                    <th>性别</th>
                                    <th>专业</th>
                                    <th>邮箱</th>
                                    <th>电话</th>
                                    <th>入学日期</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody id="students-table-body">
                                <!-- 学生数据将通过JavaScript动态加载 -->
                            </tbody>
                        </table>
                    </div>
                    
                    <!-- 分页导航 -->
                    <nav aria-label="学生列表分页">
                        <ul class="pagination justify-content-center" id="pagination">
                            <!-- 分页按钮将通过JavaScript动态生成 -->
                        </ul>
                    </nav>
                    
                    <!-- 加载状态 -->
                    <div id="loading" class="text-center py-4 d-none">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">加载中...</span>
                        </div>
                        <p class="mt-2">正在加载学生数据...</p>
                    </div>
                    
                    <!-- 空状态 -->
                    <div id="empty-state" class="text-center py-5 d-none">
                        <i class="fas fa-users fa-3x text-muted mb-3"></i>
                        <h5 class="text-muted">暂无学生数据</h5>
                        <p class="text-muted">点击上方"添加学生"按钮开始添加学生信息</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- 统计分析部分 -->
        <section id="statistics-section" class="content-section d-none">
            <div class="row">
                <div class="col-12">
                    <h2 class="mb-4">
                        <i class="fas fa-chart-bar me-2"></i>统计分析
                    </h2>
                </div>
            </div>
            
            <div class="row">
                <div class="col-lg-6 mb-4">
                    <div class="card shadow">
                        <div class="card-header">
                            <h5 class="mb-0">年龄分布统计</h5>
                        </div>
                        <div class="card-body">
                            <canvas id="ageDistributionChart" width="400" height="300"></canvas>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-6 mb-4">
                    <div class="card shadow">
                        <div class="card-header">
                            <h5 class="mb-0">入学年份统计</h5>
                        </div>
                        <div class="card-body">
                            <canvas id="enrollmentYearChart" width="400" height="300"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <!-- 学生信息模态框 -->
    <div class="modal fade" id="studentModal" tabindex="-1" aria-labelledby="studentModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="studentModalLabel">添加学生</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="关闭"></button>
                </div>
                <div class="modal-body">
                    <form id="student-form" novalidate>
                        <input type="hidden" id="student-id">
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="student-name" class="form-label">姓名 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="student-name" required>
                                <div class="invalid-feedback">请输入学生姓名</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="student-number" class="form-label">学号 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="student-number" required>
                                <div class="invalid-feedback">请输入学号</div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="student-age" class="form-label">年龄 <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" id="student-age" min="16" max="100" required>
                                <div class="invalid-feedback">请输入有效的年龄（16-100）</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="student-gender" class="form-label">性别 <span class="text-danger">*</span></label>
                                <select class="form-select" id="student-gender" required>
                                    <option value="">请选择性别</option>
                                    <option value="男">男</option>
                                    <option value="女">女</option>
                                </select>
                                <div class="invalid-feedback">请选择性别</div>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="student-major" class="form-label">专业 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="student-major" required>
                            <div class="invalid-feedback">请输入专业</div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="student-email" class="form-label">邮箱</label>
                                <input type="email" class="form-control" id="student-email">
                                <div class="invalid-feedback">请输入有效的邮箱地址</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="student-phone" class="form-label">电话</label>
                                <input type="tel" class="form-control" id="student-phone">
                                <div class="invalid-feedback">请输入有效的电话号码</div>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="student-enrollment-date" class="form-label">入学日期</label>
                            <input type="date" class="form-control" id="student-enrollment-date">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" onclick="saveStudent()">保存</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 确认删除模态框 -->
    <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmDeleteModalLabel">确认删除</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="关闭"></button>
                </div>
                <div class="modal-body">
                    <p id="delete-message">确定要删除这个学生吗？此操作不可撤销。</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-danger" id="confirm-delete-btn">确认删除</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Toast 通知 -->
    <div class="toast-container position-fixed bottom-0 end-0 p-3">
        <div id="toast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <i class="fas fa-info-circle text-primary me-2"></i>
                <strong class="me-auto">系统通知</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="关闭"></button>
            </div>
            <div class="toast-body" id="toast-message">
                <!-- 通知消息 -->
            </div>
        </div>
    </div>

    <!-- JavaScript 库 -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios@1.4.0/dist/axios.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
    
    <!-- 自定义JavaScript -->
    <script src="js/config.js"></script>
    <script src="js/api.js"></script>
    <script src="js/utils.js"></script>
    <script src="js/charts.js"></script>
    <script src="js/students.js"></script>
    <script src="js/dashboard.js"></script>
    <script src="js/app.js"></script>
</body>
</html>
```

---

## 6.3 样式设计

### 🎯 实战任务2：创建CSS样式文件

```css
/* src/main/resources/static/css/style.css */

/* ===== 全局样式 ===== */
:root {
    --primary-color: #007bff;
    --secondary-color: #6c757d;
    --success-color: #28a745;
    --danger-color: #dc3545;
    --warning-color: #ffc107;
    --info-color: #17a2b8;
    --light-color: #f8f9fa;
    --dark-color: #343a40;
    --border-radius: 0.375rem;
    --box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
    --transition: all 0.3s ease;
}

* {
    box-sizing: border-box;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    line-height: 1.6;
    color: #333;
    background-color: #f8f9fa;
}

/* ===== 导航栏样式 ===== */
.navbar {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: var(--transition);
}

.navbar-brand {
    font-weight: 600;
    font-size: 1.25rem;
}

.navbar-nav .nav-link {
    font-weight: 500;
    transition: var(--transition);
    border-radius: var(--border-radius);
    margin: 0 0.25rem;
}

.navbar-nav .nav-link:hover {
    background-color: rgba(255, 255, 255, 0.1);
    transform: translateY(-1px);
}

.navbar-nav .nav-link.active {
    background-color: rgba(255, 255, 255, 0.2);
    font-weight: 600;
}

/* ===== 卡片样式 ===== */
.card {
    border: none;
    border-radius: var(--border-radius);
    box-shadow: var(--box-shadow);
    transition: var(--transition);
    overflow: hidden;
}

.card:hover {
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
    transform: translateY(-2px);
}

.card-header {
    background-color: #fff;
    border-bottom: 1px solid #e9ecef;
    font-weight: 600;
}

/* ===== 统计卡片样式 ===== */
.border-left-primary {
    border-left: 0.25rem solid var(--primary-color) !important;
}

.border-left-success {
    border-left: 0.25rem solid var(--success-color) !important;
}

.border-left-info {
    border-left: 0.25rem solid var(--info-color) !important;
}

.border-left-warning {
    border-left: 0.25rem solid var(--warning-color) !important;
}

.text-xs {
    font-size: 0.75rem;
}

.text-gray-800 {
    color: #5a5c69 !important;
}

.text-gray-300 {
    color: #dddfeb !important;
}

/* ===== 按钮样式 ===== */
.btn {
    border-radius: var(--border-radius);
    font-weight: 500;
    transition: var(--transition);
    border: none;
    padding: 0.5rem 1rem;
}

.btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.1);
}

.btn:active {
    transform: translateY(0);
}

.btn-primary {
    background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
}

.btn-success {
    background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
}

.btn-danger {
    background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
}

.btn-warning {
    background: linear-gradient(135deg, #ffc107 0%, #e0a800 100%);
    color: #212529;
}

.btn-info {
    background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
}

/* ===== 表格样式 ===== */
.table {
    margin-bottom: 0;
}

.table th {
    border-top: none;
    font-weight: 600;
    text-transform: uppercase;
    font-size: 0.875rem;
    letter-spacing: 0.5px;
    padding: 1rem 0.75rem;
}

.table td {
    padding: 0.75rem;
    vertical-align: middle;
}

.table-hover tbody tr:hover {
    background-color: rgba(0, 123, 255, 0.05);
    transition: var(--transition);
}

.table-dark th {
    background-color: var(--dark-color);
    border-color: #454d55;
}

/* ===== 表单样式 ===== */
.form-control,
.form-select {
    border-radius: var(--border-radius);
    border: 1px solid #ced4da;
    transition: var(--transition);
    padding: 0.5rem 0.75rem;
}

.form-control:focus,
.form-select:focus {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.form-label {
    font-weight: 500;
    margin-bottom: 0.5rem;
    color: #495057;
}

.invalid-feedback {
    font-size: 0.875rem;
}

/* ===== 模态框样式 ===== */
.modal-content {
    border: none;
    border-radius: var(--border-radius);
    box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.175);
}

.modal-header {
    border-bottom: 1px solid #e9ecef;
    padding: 1.5rem;
}

.modal-body {
    padding: 1.5rem;
}

.modal-footer {
    border-top: 1px solid #e9ecef;
    padding: 1.5rem;
}

.modal-title {
    font-weight: 600;
}

/* ===== 分页样式 ===== */
.pagination {
    margin-bottom: 0;
}

.page-link {
    border-radius: var(--border-radius);
    margin: 0 0.125rem;
    border: 1px solid #dee2e6;
    transition: var(--transition);
}

.page-link:hover {
    background-color: var(--primary-color);
    border-color: var(--primary-color);
    color: white;
    transform: translateY(-1px);
}

.page-item.active .page-link {
    background-color: var(--primary-color);
    border-color: var(--primary-color);
}

/* ===== Toast 通知样式 ===== */
.toast {
    border: none;
    border-radius: var(--border-radius);
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
}

.toast-header {
    background-color: #fff;
    border-bottom: 1px solid #e9ecef;
}

/* ===== 加载动画 ===== */
.spinner-border {
    width: 3rem;
    height: 3rem;
}

/* ===== 空状态样式 ===== */
#empty-state {
    color: #6c757d;
}

#empty-state i {
    opacity: 0.5;
}

/* ===== 内容区域样式 ===== */
.content-section {
    animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* ===== 响应式设计 ===== */
@media (max-width: 768px) {
    .container-fluid {
        padding-left: 1rem;
        padding-right: 1rem;
    }
    
    .card {
        margin-bottom: 1rem;
    }
    
    .table-responsive {
        font-size: 0.875rem;
    }
    
    .btn {
        padding: 0.375rem 0.75rem;
        font-size: 0.875rem;
    }
    
    .modal-dialog {
        margin: 1rem;
    }
    
    .navbar-brand {
        font-size: 1.1rem;
    }
    
    .h5 {
        font-size: 1.1rem;
    }
}

@media (max-width: 576px) {
    .col-xl-3,
    .col-md-6 {
        margin-bottom: 1rem;
    }
    
    .table th,
    .table td {
        padding: 0.5rem 0.25rem;
        font-size: 0.8rem;
    }
    
    .btn-group-sm > .btn,
    .btn-sm {
        padding: 0.25rem 0.5rem;
        font-size: 0.75rem;
    }
}

/* ===== 自定义滚动条 ===== */
::-webkit-scrollbar {
    width: 8px;
    height: 8px;
}

::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 4px;
}

::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 4px;
    transition: var(--transition);
}

::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
}

/* ===== 工具类 ===== */
.shadow-sm {
    box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075) !important;
}

.shadow {
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}

.shadow-lg {
    box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.175) !important;
}

.border-0 {
    border: 0 !important;
}

.rounded {
    border-radius: var(--border-radius) !important;
}

.text-truncate {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

/* ===== 动画效果 ===== */
.fade-in {
    animation: fadeIn 0.5s ease-in-out;
}

.slide-in-up {
    animation: slideInUp 0.5s ease-out;
}

@keyframes slideInUp {
    from {
        transform: translateY(30px);
        opacity: 0;
    }
    to {
        transform: translateY(0);
        opacity: 1;
    }
}

.bounce-in {
    animation: bounceIn 0.6s ease-out;
}

@keyframes bounceIn {
    0% {
        transform: scale(0.3);
        opacity: 0;
    }
    50% {
        transform: scale(1.05);
    }
    70% {
        transform: scale(0.9);
    }
    100% {
        transform: scale(1);
        opacity: 1;
    }
}

/* ===== 打印样式 ===== */
@media print {
    .navbar,
    .btn,
    .modal,
    .toast-container {
        display: none !important;
    }
    
    .card {
        box-shadow: none;
        border: 1px solid #dee2e6;
    }
    
    .table {
        font-size: 0.875rem;
    }
    
    body {
        background-color: white;
    }
}

/* ===== 可访问性增强 ===== */
.sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border: 0;
}

/* 焦点样式 */
.btn:focus,
.form-control:focus,
.form-select:focus {
    outline: 2px solid var(--primary-color);
    outline-offset: 2px;
}

/* 高对比度模式支持 */
@media (prefers-contrast: high) {
    .card {
        border: 2px solid #000;
    }
    
    .btn {
        border: 2px solid currentColor;
    }
}

/* 减少动画模式支持 */
@media (prefers-reduced-motion: reduce) {
    *,
    *::before,
    *::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        transition-duration: 0.01ms !important;
    }
}
```

---

## 6.4 JavaScript交互逻辑

### 🎯 实战任务3：创建配置文件

```javascript
// src/main/resources/static/js/config.js

/**
 * 应用配置文件
 */
const CONFIG = {
    // API基础URL
    API_BASE_URL: '/api',
    
    // 分页配置
    PAGINATION: {
        DEFAULT_PAGE_SIZE: 10,
        PAGE_SIZE_OPTIONS: [5, 10, 20, 50],
        MAX_PAGE_BUTTONS: 5
    },
    
    // 表格配置
    TABLE: {
        DEFAULT_SORT_FIELD: 'id',
        DEFAULT_SORT_DIRECTION: 'asc'
    },
    
    // 验证规则
    VALIDATION: {
        NAME_MIN_LENGTH: 2,
        NAME_MAX_LENGTH: 50,
        STUDENT_NUMBER_PATTERN: /^\d{8}$/,
        AGE_MIN: 16,
        AGE_MAX: 100,
        EMAIL_PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
        PHONE_PATTERN: /^1[3-9]\d{9}$/
    },
    
    // 消息配置
    MESSAGES: {
        SUCCESS: {
            STUDENT_CREATED: '学生信息创建成功',
            STUDENT_UPDATED: '学生信息更新成功',
            STUDENT_DELETED: '学生信息删除成功',
            STUDENTS_DELETED: '批量删除成功'
        },
        ERROR: {
            NETWORK_ERROR: '网络连接失败，请检查网络设置',
            SERVER_ERROR: '服务器错误，请稍后重试',
            VALIDATION_ERROR: '数据验证失败，请检查输入',
            NOT_FOUND: '未找到指定的学生信息',
            DUPLICATE_STUDENT_NUMBER: '学号已存在，请使用其他学号'
        },
        CONFIRM: {
            DELETE_STUDENT: '确定要删除这个学生吗？此操作不可撤销。',
            DELETE_STUDENTS: '确定要删除选中的 {count} 个学生吗？此操作不可撤销。'
        }
    },
    
    // 动画配置
    ANIMATION: {
        DURATION: 300,
        EASING: 'ease-in-out'
    },
    
    // 图表配置
    CHARTS: {
        COLORS: {
            PRIMARY: '#007bff',
            SUCCESS: '#28a745',
            DANGER: '#dc3545',
            WARNING: '#ffc107',
            INFO: '#17a2b8',
            SECONDARY: '#6c757d'
        },
        DEFAULT_OPTIONS: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top'
                }
            }
        }
    },
    
    // 本地存储键名
    STORAGE_KEYS: {
        USER_PREFERENCES: 'student_management_preferences',
        LAST_SEARCH: 'student_management_last_search',
        TABLE_SETTINGS: 'student_management_table_settings'
    }
};

// 冻结配置对象，防止意外修改
Object.freeze(CONFIG);
```

### 🎯 实战任务4：创建API服务

```javascript
// src/main/resources/static/js/api.js

/**
 * API服务类
 * 封装所有与后端API的交互
 */
class ApiService {
    constructor() {
        this.baseURL = CONFIG.API_BASE_URL;
        this.setupAxios();
    }
    
    /**
     * 配置Axios
     */
    setupAxios() {
        // 请求拦截器
        axios.interceptors.request.use(
            config => {
                // 显示加载状态
                this.showLoading();
                
                // 设置请求头
                config.headers['Content-Type'] = 'application/json';
                config.headers['Accept'] = 'application/json';
                
                return config;
            },
            error => {
                this.hideLoading();
                return Promise.reject(error);
            }
        );
        
        // 响应拦截器
        axios.interceptors.response.use(
            response => {
                this.hideLoading();
                return response;
            },
            error => {
                this.hideLoading();
                this.handleError(error);
                return Promise.reject(error);
            }
        );
    }
    
    /**
     * 显示加载状态
     */
    showLoading() {
        const loading = document.getElementById('loading');
        if (loading) {
            loading.classList.remove('d-none');
        }
    }
    
    /**
     * 隐藏加载状态
     */
    hideLoading() {
        const loading = document.getElementById('loading');
        if (loading) {
            loading.classList.add('d-none');
        }
    }
    
    /**
     * 处理API错误
     */
    handleError(error) {
        let message = CONFIG.MESSAGES.ERROR.NETWORK_ERROR;
        
        if (error.response) {
            // 服务器响应错误
            const status = error.response.status;
            const data = error.response.data;
            
            switch (status) {
                case 400:
                    message = data.message || CONFIG.MESSAGES.ERROR.VALIDATION_ERROR;
                    break;
                case 404:
                    message = CONFIG.MESSAGES.ERROR.NOT_FOUND;
                    break;
                case 409:
                    message = CONFIG.MESSAGES.ERROR.DUPLICATE_STUDENT_NUMBER;
                    break;
                case 500:
                    message = CONFIG.MESSAGES.ERROR.SERVER_ERROR;
                    break;
                default:
                    message = data.message || CONFIG.MESSAGES.ERROR.SERVER_ERROR;
            }
        } else if (error.request) {
            // 网络错误
            message = CONFIG.MESSAGES.ERROR.NETWORK_ERROR;
        }
        
        Utils.showToast(message, 'error');
    }
    
    // ===== 学生相关API =====
    
    /**
     * 获取学生列表（分页）
     */
    async getStudents(params = {}) {
        const queryParams = new URLSearchParams({
            page: params.page || 0,
            size: params.size || CONFIG.PAGINATION.DEFAULT_PAGE_SIZE,
            sort: params.sort || CONFIG.TABLE.DEFAULT_SORT_FIELD,
            direction: params.direction || CONFIG.TABLE.DEFAULT_SORT_DIRECTION
        });
        
        const response = await axios.get(`${this.baseURL}/students?${queryParams}`);
        return response.data;
    }
    
    /**
     * 搜索学生
     */
    async searchStudents(searchParams) {
        const queryParams = new URLSearchParams();
        
        Object.keys(searchParams).forEach(key => {
            if (searchParams[key] !== null && searchParams[key] !== undefined && searchParams[key] !== '') {
                queryParams.append(key, searchParams[key]);
            }
        });
        
        const response = await axios.get(`${this.baseURL}/students/search?${queryParams}`);
        return response.data;
    }
    
    /**
     * 根据ID获取学生
     */
    async getStudentById(id) {
        const response = await axios.get(`${this.baseURL}/students/${id}`);
        return response.data;
    }
    
    /**
     * 根据学号获取学生
     */
    async getStudentByNumber(studentNumber) {
        const response = await axios.get(`${this.baseURL}/students/number/${studentNumber}`);
        return response.data;
    }
    
    /**
     * 创建学生
     */
    async createStudent(studentData) {
        const response = await axios.post(`${this.baseURL}/students`, studentData);
        return response.data;
    }
    
    /**
     * 更新学生
     */
    async updateStudent(id, studentData) {
        const response = await axios.put(`${this.baseURL}/students/${id}`, studentData);
        return response.data;
    }
    
    /**
     * 删除学生
     */
    async deleteStudent(id) {
        const response = await axios.delete(`${this.baseURL}/students/${id}`);
        return response.data;
    }
    
    /**
     * 批量删除学生
     */
    async deleteStudents(ids) {
        const response = await axios.delete(`${this.baseURL}/students/batch`, {
            data: ids
        });
        return response.data;
    }
    
    /**
     * 检查学号是否存在
     */
    async checkStudentNumberExists(studentNumber) {
        const response = await axios.get(`${this.baseURL}/students/exists/${studentNumber}`);
        return response.data;
    }
    
    // ===== 统计相关API =====
    
    /**
     * 获取学生统计信息
     */
    async getStudentStatistics() {
        const response = await axios.get(`${this.baseURL}/students/statistics`);
        return response.data;
    }
    
    /**
     * 获取专业分布统计
     */
    async getMajorStatistics() {
        const response = await axios.get(`${this.baseURL}/students/statistics/major`);
        return response.data;
    }
    
    /**
     * 获取年龄分布统计
     */
    async getAgeDistribution() {
        const response = await axios.get(`${this.baseURL}/students/statistics/age`);
        return response.data;
    }
}

// 创建API服务实例
const apiService = new ApiService();
```

### 🎯 实战任务5：创建工具函数

```javascript
// src/main/resources/static/js/utils.js

/**
 * 工具函数类
 */
class Utils {
    /**
     * 显示Toast通知
     */
    static showToast(message, type = 'info', duration = 3000) {
        const toast = document.getElementById('toast');
        const toastMessage = document.getElementById('toast-message');
        const toastHeader = toast.querySelector('.toast-header');
        
        if (!toast || !toastMessage) return;
        
        // 设置消息内容
        toastMessage.textContent = message;
        
        // 设置图标和颜色
        const icon = toastHeader.querySelector('i');
        icon.className = this.getToastIcon(type);
        
        // 设置样式
        toast.className = `toast ${this.getToastClass(type)}`;
        
        // 显示Toast
        const bsToast = new bootstrap.Toast(toast, {
            delay: duration
        });
        bsToast.show();
    }
    
    /**
     * 获取Toast图标
     */
    static getToastIcon(type) {
        const icons = {
            success: 'fas fa-check-circle text-success me-2',
            error: 'fas fa-exclamation-circle text-danger me-2',
            warning: 'fas fa-exclamation-triangle text-warning me-2',
            info: 'fas fa-info-circle text-primary me-2'
        };
        return icons[type] || icons.info;
    }
    
    /**
     * 获取Toast样式类
     */
    static getToastClass(type) {
        const classes = {
            success: 'toast border-success',
            error: 'toast border-danger',
            warning: 'toast border-warning',
            info: 'toast border-primary'
        };
        return classes[type] || classes.info;
    }
    
    /**
     * 格式化日期
     */
    static formatDate(dateString, format = 'YYYY-MM-DD') {
        if (!dateString) return '';
        
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return '';
        
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        
        switch (format) {
            case 'YYYY-MM-DD':
                return `${year}-${month}-${day}`;
            case 'MM/DD/YYYY':
                return `${month}/${day}/${year}`;
            case 'DD/MM/YYYY':
                return `${day}/${month}/${year}`;
            default:
                return `${year}-${month}-${day}`;
        }
    }
    
    /**
     * 格式化时间
     */
    static formatDateTime(dateString) {
        if (!dateString) return '';
        
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return '';
        
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    }
    
    /**
     * 防抖函数
     */
    static debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
    
    /**
     * 节流函数
     */
    static throttle(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }
    
    /**
     * 验证表单
     */
    static validateForm(formElement) {
        if (!formElement) return false;
        
        const inputs = formElement.querySelectorAll('input, select, textarea');
        let isValid = true;
        
        inputs.forEach(input => {
            if (!this.validateField(input)) {
                isValid = false;
            }
        });
        
        return isValid;
    }
    
    /**
     * 验证单个字段
     */
    static validateField(field) {
        const value = field.value.trim();
        const type = field.type;
        const required = field.hasAttribute('required');
        
        // 清除之前的验证状态
        field.classList.remove('is-valid', 'is-invalid');
        
        // 必填验证
        if (required && !value) {
            this.setFieldInvalid(field, '此字段为必填项');
            return false;
        }
        
        // 如果字段为空且非必填，跳过其他验证
        if (!value && !required) {
            this.setFieldValid(field);
            return true;
        }
        
        // 根据字段类型进行验证
        switch (type) {
            case 'email':
                if (!CONFIG.VALIDATION.EMAIL_PATTERN.test(value)) {
                    this.setFieldInvalid(field, '请输入有效的邮箱地址');
                    return false;
                }
                break;
                
            case 'tel':
                if (!CONFIG.VALIDATION.PHONE_PATTERN.test(value)) {
                    this.setFieldInvalid(field, '请输入有效的手机号码');
                    return false;
                }
                break;
                
            case 'number':
                const num = parseInt(value);
                const min = parseInt(field.getAttribute('min'));
                const max = parseInt(field.getAttribute('max'));
                
                if (isNaN(num)) {
                    this.setFieldInvalid(field, '请输入有效的数字');
                    return false;
                }
                
                if (min !== null && num < min) {
                    this.setFieldInvalid(field, `值不能小于${min}`);
                    return false;
                }
                
                if (max !== null && num > max) {
                    this.setFieldInvalid(field, `值不能大于${max}`);
                    return false;
                }
                break;
                
            case 'text':
                // 特殊字段验证
                if (field.id === 'student-name') {
                    if (value.length < CONFIG.VALIDATION.NAME_MIN_LENGTH) {
                        this.setFieldInvalid(field, `姓名长度不能少于${CONFIG.VALIDATION.NAME_MIN_LENGTH}个字符`);
                        return false;
                    }
                    if (value.length > CONFIG.VALIDATION.NAME_MAX_LENGTH) {
                        this.setFieldInvalid(field, `姓名长度不能超过${CONFIG.VALIDATION.NAME_MAX_LENGTH}个字符`);
                        return false;
                    }
                } else if (field.id === 'student-number') {
                    if (!CONFIG.VALIDATION.STUDENT_NUMBER_PATTERN.test(value)) {
                        this.setFieldInvalid(field, '学号必须是8位数字');
                        return false;
                    }
                }
                break;
        }
        
        this.setFieldValid(field);
        return true;
    }
    
    /**
     * 设置字段为有效状态
     */
    static setFieldValid(field) {
        field.classList.add('is-valid');
        field.classList.remove('is-invalid');
    }
    
    /**
     * 设置字段为无效状态
     */
    static setFieldInvalid(field, message) {
        field.classList.add('is-invalid');
        field.classList.remove('is-valid');
        
        const feedback = field.parentNode.querySelector('.invalid-feedback');
        if (feedback) {
            feedback.textContent = message;
        }
    }
    
    /**
     * 生成分页HTML
     */
    static generatePagination(currentPage, totalPages, onPageClick) {
        const pagination = document.getElementById('pagination');
        if (!pagination) return;
        
        pagination.innerHTML = '';
        
        if (totalPages <= 1) return;
        
        const maxButtons = CONFIG.PAGINATION.MAX_PAGE_BUTTONS;
        let startPage = Math.max(0, currentPage - Math.floor(maxButtons / 2));
        let endPage = Math.min(totalPages - 1, startPage + maxButtons - 1);
        
        if (endPage - startPage < maxButtons - 1) {
            startPage = Math.max(0, endPage - maxButtons + 1);
        }
        
        // 上一页按钮
        const prevLi = document.createElement('li');
        prevLi.className = `page-item ${currentPage === 0 ? 'disabled' : ''}`;
        prevLi.innerHTML = `<a class="page-link" href="#" data-page="${currentPage - 1}">上一页</a>`;
        pagination.appendChild(prevLi);
        
        // 页码按钮
        for (let i = startPage; i <= endPage; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#" data-page="${i}">${i + 1}</a>`;
            pagination.appendChild(li);
        }
        
        // 下一页按钮
        const nextLi = document.createElement('li');
        nextLi.className = `page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}`;
        nextLi.innerHTML = `<a class="page-link" href="#" data-page="${currentPage + 1}">下一页</a>`;
        pagination.appendChild(nextLi);
        
        // 绑定点击事件
        pagination.addEventListener('click', (e) => {
            e.preventDefault();
            const link = e.target.closest('.page-link');
            if (link && !link.parentNode.classList.contains('disabled')) {
                const page = parseInt(link.dataset.page);
                if (!isNaN(page) && page >= 0 && page < totalPages) {
                    onPageClick(page);
                }
            }
        });
    }
    
    /**
     * 本地存储操作
     */
    static saveToStorage(key, data) {
        try {
            localStorage.setItem(key, JSON.stringify(data));
        } catch (error) {
            console.warn('无法保存到本地存储:', error);
        }
    }
    
    static getFromStorage(key, defaultValue = null) {
        try {
            const data = localStorage.getItem(key);
            return data ? JSON.parse(data) : defaultValue;
        } catch (error) {
            console.warn('无法从本地存储读取:', error);
            return defaultValue;
        }
    }
    
    /**
     * 深拷贝对象
     */
    static deepClone(obj) {
        if (obj === null || typeof obj !== 'object') return obj;
        if (obj instanceof Date) return new Date(obj.getTime());
        if (obj instanceof Array) return obj.map(item => this.deepClone(item));
        if (typeof obj === 'object') {
            const clonedObj = {};
            for (const key in obj) {
                if (obj.hasOwnProperty(key)) {
                    clonedObj[key] = this.deepClone(obj[key]);
                }
            }
            return clonedObj;
        }
    }
    
    /**
     * 转义HTML
     */
    static escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    /**
     * 生成随机ID
     */
    static generateId() {
        return Math.random().toString(36).substr(2, 9);
    }
}

---

## 6.5 图表组件开发

### 🎯 实战任务6：创建图表服务

```javascript
// src/main/resources/static/js/charts.js

/**
 * 图表服务类
 */
class ChartService {
    constructor() {
        this.charts = new Map();
    }
    
    /**
     * 创建专业分布柱状图
     */
    createMajorChart(canvasId, data) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return null;
        
        // 销毁已存在的图表
        this.destroyChart(canvasId);
        
        const chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map(item => item.major),
                datasets: [{
                    label: '学生人数',
                    data: data.map(item => item.count),
                    backgroundColor: CONFIG.CHARTS.COLORS.PRIMARY,
                    borderColor: CONFIG.CHARTS.COLORS.PRIMARY,
                    borderWidth: 1,
                    borderRadius: 4,
                    borderSkipped: false
                }]
            },
            options: {
                ...CONFIG.CHARTS.DEFAULT_OPTIONS,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return `${context.label}: ${context.parsed.y}人`;
                            }
                        }
                    }
                }
            }
        });
        
        this.charts.set(canvasId, chart);
        return chart;
    }
    
    /**
     * 创建性别分布饼图
     */
    createGenderChart(canvasId, data) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return null;
        
        this.destroyChart(canvasId);
        
        const chart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: data.map(item => item.gender),
                datasets: [{
                    data: data.map(item => item.count),
                    backgroundColor: [
                        CONFIG.CHARTS.COLORS.PRIMARY,
                        CONFIG.CHARTS.COLORS.SUCCESS
                    ],
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                ...CONFIG.CHARTS.DEFAULT_OPTIONS,
                cutout: '60%',
                plugins: {
                    legend: {
                        position: 'bottom'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = ((context.parsed / total) * 100).toFixed(1);
                                return `${context.label}: ${context.parsed}人 (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
        
        this.charts.set(canvasId, chart);
        return chart;
    }
    
    /**
     * 创建年龄分布直方图
     */
    createAgeDistributionChart(canvasId, data) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return null;
        
        this.destroyChart(canvasId);
        
        const chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map(item => `${item.ageRange}岁`),
                datasets: [{
                    label: '学生人数',
                    data: data.map(item => item.count),
                    backgroundColor: CONFIG.CHARTS.COLORS.INFO,
                    borderColor: CONFIG.CHARTS.COLORS.INFO,
                    borderWidth: 1
                }]
            },
            options: {
                ...CONFIG.CHARTS.DEFAULT_OPTIONS,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
        
        this.charts.set(canvasId, chart);
        return chart;
    }
    
    /**
     * 创建入学年份折线图
     */
    createEnrollmentYearChart(canvasId, data) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return null;
        
        this.destroyChart(canvasId);
        
        const chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: data.map(item => item.year),
                datasets: [{
                    label: '入学人数',
                    data: data.map(item => item.count),
                    borderColor: CONFIG.CHARTS.COLORS.WARNING,
                    backgroundColor: CONFIG.CHARTS.COLORS.WARNING + '20',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                ...CONFIG.CHARTS.DEFAULT_OPTIONS,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
        
        this.charts.set(canvasId, chart);
        return chart;
    }
    
    /**
     * 销毁图表
     */
    destroyChart(canvasId) {
        const existingChart = this.charts.get(canvasId);
        if (existingChart) {
            existingChart.destroy();
            this.charts.delete(canvasId);
        }
    }
    
    /**
     * 销毁所有图表
     */
    destroyAllCharts() {
        this.charts.forEach(chart => chart.destroy());
        this.charts.clear();
    }
    
    /**
     * 更新图表数据
     */
    updateChart(canvasId, newData) {
        const chart = this.charts.get(canvasId);
        if (chart) {
            chart.data = newData;
            chart.update();
        }
    }
}

// 创建图表服务实例
const chartService = new ChartService();
```

---

## 6.6 学生管理模块

### 🎯 实战任务7：创建学生管理JavaScript

```javascript
// src/main/resources/static/js/students.js

/**
 * 学生管理类
 */
class StudentManager {
    constructor() {
        this.currentPage = 0;
        this.pageSize = CONFIG.PAGINATION.DEFAULT_PAGE_SIZE;
        this.sortField = CONFIG.TABLE.DEFAULT_SORT_FIELD;
        this.sortDirection = CONFIG.TABLE.DEFAULT_SORT_DIRECTION;
        this.searchParams = {};
        this.selectedStudents = new Set();
        
        this.initializeEventListeners();
        this.loadStudents();
        this.loadMajorOptions();
    }
    
    /**
     * 初始化事件监听器
     */
    initializeEventListeners() {
        // 搜索输入框
        const searchInput = document.getElementById('search-input');
        if (searchInput) {
            searchInput.addEventListener('input', 
                Utils.debounce(() => this.handleSearch(), 500)
            );
            searchInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    this.handleSearch();
                }
            });
        }
        
        // 筛选器
        ['major-filter', 'gender-filter', 'sort-field', 'sort-direction'].forEach(id => {
            const element = document.getElementById(id);
            if (element) {
                element.addEventListener('change', () => this.handleFilterChange());
            }
        });
        
        // 确认删除按钮
        const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
        if (confirmDeleteBtn) {
            confirmDeleteBtn.addEventListener('click', () => this.confirmDelete());
        }
    }
    
    /**
     * 加载学生列表
     */
    async loadStudents() {
        try {
            this.showLoading();
            
            const params = {
                page: this.currentPage,
                size: this.pageSize,
                sort: this.sortField,
                direction: this.sortDirection,
                ...this.searchParams
            };
            
            const response = await apiService.getStudents(params);
            
            if (response.success) {
                this.renderStudentsTable(response.data.content);
                this.renderPagination(response.data);
                this.updateSelectedCount();
            }
        } catch (error) {
            console.error('加载学生列表失败:', error);
        } finally {
            this.hideLoading();
        }
    }
    
    /**
     * 渲染学生表格
     */
    renderStudentsTable(students) {
        const tbody = document.getElementById('students-table-body');
        const emptyState = document.getElementById('empty-state');
        
        if (!tbody) return;
        
        if (students.length === 0) {
            tbody.innerHTML = '';
            if (emptyState) emptyState.classList.remove('d-none');
            return;
        }
        
        if (emptyState) emptyState.classList.add('d-none');
        
        tbody.innerHTML = students.map(student => `
            <tr>
                <td>
                    <input type="checkbox" class="student-checkbox" 
                           value="${student.id}" 
                           onchange="studentManager.handleStudentSelect(${student.id}, this.checked)">
                </td>
                <td>${student.id}</td>
                <td>${Utils.escapeHtml(student.name)}</td>
                <td>${Utils.escapeHtml(student.studentNumber)}</td>
                <td>${student.age}</td>
                <td>${Utils.escapeHtml(student.gender)}</td>
                <td>${Utils.escapeHtml(student.major)}</td>
                <td>${Utils.escapeHtml(student.email || '')}</td>
                <td>${Utils.escapeHtml(student.phone || '')}</td>
                <td>${Utils.formatDate(student.enrollmentDate)}</td>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <button type="button" class="btn btn-outline-primary" 
                                onclick="studentManager.editStudent(${student.id})" 
                                title="编辑">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-outline-danger" 
                                onclick="studentManager.deleteStudent(${student.id})" 
                                title="删除">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');
    }
    
    /**
     * 渲染分页
     */
    renderPagination(pageData) {
        Utils.generatePagination(
            pageData.number,
            pageData.totalPages,
            (page) => {
                this.currentPage = page;
                this.loadStudents();
            }
        );
    }
    
    /**
     * 处理搜索
     */
    handleSearch() {
        const searchInput = document.getElementById('search-input');
        const keyword = searchInput ? searchInput.value.trim() : '';
        
        if (keyword) {
            this.searchParams.keyword = keyword;
        } else {
            delete this.searchParams.keyword;
        }
        
        this.currentPage = 0;
        this.loadStudents();
    }
    
    /**
     * 处理筛选器变化
     */
    handleFilterChange() {
        const majorFilter = document.getElementById('major-filter');
        const genderFilter = document.getElementById('gender-filter');
        const sortField = document.getElementById('sort-field');
        const sortDirection = document.getElementById('sort-direction');
        
        // 更新搜索参数
        if (majorFilter && majorFilter.value) {
            this.searchParams.major = majorFilter.value;
        } else {
            delete this.searchParams.major;
        }
        
        if (genderFilter && genderFilter.value) {
            this.searchParams.gender = genderFilter.value;
        } else {
            delete this.searchParams.gender;
        }
        
        // 更新排序参数
        if (sortField) this.sortField = sortField.value;
        if (sortDirection) this.sortDirection = sortDirection.value;
        
        this.currentPage = 0;
        this.loadStudents();
    }
    
    /**
     * 处理学生选择
     */
    handleStudentSelect(studentId, checked) {
        if (checked) {
            this.selectedStudents.add(studentId);
        } else {
            this.selectedStudents.delete(studentId);
        }
        
        this.updateSelectedCount();
        this.updateSelectAllCheckbox();
    }
    
    /**
     * 更新选中数量
     */
    updateSelectedCount() {
        const deleteBtn = document.getElementById('delete-selected-btn');
        if (deleteBtn) {
            deleteBtn.disabled = this.selectedStudents.size === 0;
            deleteBtn.textContent = this.selectedStudents.size > 0 
                ? `批量删除 (${this.selectedStudents.size})` 
                : '批量删除';
        }
    }
    
    /**
     * 更新全选复选框状态
     */
    updateSelectAllCheckbox() {
        const selectAllCheckbox = document.getElementById('select-all-checkbox');
        const studentCheckboxes = document.querySelectorAll('.student-checkbox');
        
        if (selectAllCheckbox && studentCheckboxes.length > 0) {
            const checkedCount = this.selectedStudents.size;
            const totalCount = studentCheckboxes.length;
            
            selectAllCheckbox.checked = checkedCount === totalCount;
            selectAllCheckbox.indeterminate = checkedCount > 0 && checkedCount < totalCount;
        }
    }
    
    /**
     * 显示加载状态
     */
    showLoading() {
        const loading = document.getElementById('loading');
        const emptyState = document.getElementById('empty-state');
        
        if (loading) loading.classList.remove('d-none');
        if (emptyState) emptyState.classList.add('d-none');
    }
    
    /**
     * 隐藏加载状态
     */
    hideLoading() {
        const loading = document.getElementById('loading');
        if (loading) loading.classList.add('d-none');
    }
    
    /**
     * 加载专业选项
     */
    async loadMajorOptions() {
        try {
            const response = await apiService.getMajorStatistics();
            if (response.success) {
                const majorFilter = document.getElementById('major-filter');
                if (majorFilter) {
                    const options = response.data.map(item => 
                        `<option value="${Utils.escapeHtml(item.major)}">${Utils.escapeHtml(item.major)}</option>`
                    ).join('');
                    majorFilter.innerHTML = '<option value="">所有专业</option>' + options;
                }
            }
        } catch (error) {
            console.error('加载专业选项失败:', error);
        }
    }
}

// 学生表单相关函数
function openAddModal() {
    const modal = document.getElementById('studentModal');
    const modalTitle = document.getElementById('studentModalLabel');
    const form = document.getElementById('student-form');
    
    if (modalTitle) modalTitle.textContent = '添加学生';
    if (form) form.reset();
    
    // 清除验证状态
    const inputs = form.querySelectorAll('input, select');
    inputs.forEach(input => {
        input.classList.remove('is-valid', 'is-invalid');
    });
    
    // 清除隐藏的ID字段
    const idField = document.getElementById('student-id');
    if (idField) idField.value = '';
}

function editStudent(id) {
    // 实现编辑学生功能
    // 这里需要调用API获取学生详情并填充表单
}

function saveStudent() {
    // 实现保存学生功能
    // 这里需要验证表单并调用API
}

function deleteStudent(id) {
    // 实现删除单个学生功能
}

function deleteSelectedStudents() {
    // 实现批量删除功能
}

function toggleSelectAll() {
    // 实现全选/取消全选功能
}

function searchStudents() {
    // 实现搜索功能
    if (window.studentManager) {
        window.studentManager.handleSearch();
    }
}

// 创建学生管理器实例
let studentManager;
document.addEventListener('DOMContentLoaded', () => {
    studentManager = new StudentManager();
    window.studentManager = studentManager;
});
```

---

## 6.7 仪表盘模块

### 🎯 实战任务8：创建仪表盘JavaScript

```javascript
// src/main/resources/static/js/dashboard.js

/**
 * 仪表盘管理类
 */
class DashboardManager {
    constructor() {
        this.statisticsData = null;
        this.charts = {
            majorChart: null,
            genderChart: null,
            ageDistributionChart: null,
            enrollmentYearChart: null
        };
        
        this.loadDashboardData();
    }
    
    /**
     * 加载仪表盘数据
     */
    async loadDashboardData() {
        try {
            await Promise.all([
                this.loadStatistics(),
                this.loadMajorStatistics(),
                this.loadAgeDistribution(),
                this.loadEnrollmentYearData()
            ]);
        } catch (error) {
            console.error('加载仪表盘数据失败:', error);
            Utils.showToast('加载仪表盘数据失败', 'error');
        }
    }
    
    /**
     * 加载基础统计数据
     */
    async loadStatistics() {
        try {
            const response = await apiService.getStudentStatistics();
            if (response.success) {
                this.statisticsData = response.data;
                this.updateStatisticsCards();
            }
        } catch (error) {
            console.error('加载统计数据失败:', error);
        }
    }
    
    /**
     * 更新统计卡片
     */
    updateStatisticsCards() {
        if (!this.statisticsData) return;
        
        const elements = {
            'total-students': this.statisticsData.totalStudents || 0,
            'total-majors': this.statisticsData.totalMajors || 0,
            'average-age': this.statisticsData.averageAge ? this.statisticsData.averageAge.toFixed(1) : '0',
            'today-new': this.statisticsData.todayNew || 0
        };
        
        Object.entries(elements).forEach(([id, value]) => {
            const element = document.getElementById(id);
            if (element) {
                // 添加数字动画效果
                this.animateNumber(element, 0, value, 1000);
            }
        });
    }
    
    /**
     * 数字动画效果
     */
    animateNumber(element, start, end, duration) {
        const startTime = performance.now();
        const isFloat = typeof end === 'string' && end.includes('.');
        const endValue = parseFloat(end);
        
        const animate = (currentTime) => {
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / duration, 1);
            
            const current = start + (endValue - start) * this.easeOutCubic(progress);
            
            if (isFloat) {
                element.textContent = current.toFixed(1);
            } else {
                element.textContent = Math.floor(current);
            }
            
            if (progress < 1) {
                requestAnimationFrame(animate);
            } else {
                element.textContent = end;
            }
        };
        
        requestAnimationFrame(animate);
    }
    
    /**
     * 缓动函数
     */
    easeOutCubic(t) {
        return 1 - Math.pow(1 - t, 3);
    }
    
    /**
     * 加载专业统计数据
     */
    async loadMajorStatistics() {
        try {
            const response = await apiService.getMajorStatistics();
            if (response.success && response.data.length > 0) {
                this.charts.majorChart = chartService.createMajorChart('majorChart', response.data);
                
                // 创建性别分布数据（模拟）
                const genderData = [
                    { gender: '男', count: Math.floor(this.statisticsData?.totalStudents * 0.6) || 0 },
                    { gender: '女', count: Math.floor(this.statisticsData?.totalStudents * 0.4) || 0 }
                ];
                this.charts.genderChart = chartService.createGenderChart('genderChart', genderData);
            }
        } catch (error) {
            console.error('加载专业统计失败:', error);
        }
    }
    
    /**
     * 加载年龄分布数据
     */
    async loadAgeDistribution() {
        try {
            const response = await apiService.getAgeDistribution();
            if (response.success && response.data.length > 0) {
                this.charts.ageDistributionChart = chartService.createAgeDistributionChart(
                    'ageDistributionChart', 
                    response.data
                );
            }
        } catch (error) {
            console.error('加载年龄分布失败:', error);
            // 如果API不存在，创建模拟数据
            const mockAgeData = [
                { ageRange: '18-20', count: 15 },
                { ageRange: '21-23', count: 25 },
                { ageRange: '24-26', count: 18 },
                { ageRange: '27-30', count: 8 }
            ];
            this.charts.ageDistributionChart = chartService.createAgeDistributionChart(
                'ageDistributionChart', 
                mockAgeData
            );
        }
    }
    
    /**
     * 加载入学年份数据
     */
    async loadEnrollmentYearData() {
        try {
            // 创建模拟的入学年份数据
            const currentYear = new Date().getFullYear();
            const enrollmentData = [];
            
            for (let i = 4; i >= 0; i--) {
                const year = currentYear - i;
                const count = Math.floor(Math.random() * 20) + 10;
                enrollmentData.push({ year: year.toString(), count });
            }
            
            this.charts.enrollmentYearChart = chartService.createEnrollmentYearChart(
                'enrollmentYearChart', 
                enrollmentData
            );
        } catch (error) {
            console.error('加载入学年份数据失败:', error);
        }
    }
    
    /**
     * 刷新仪表盘数据
     */
    async refresh() {
        // 销毁现有图表
        Object.values(this.charts).forEach(chart => {
            if (chart) {
                chart.destroy();
            }
        });
        
        // 重新加载数据
        await this.loadDashboardData();
        
        Utils.showToast('仪表盘数据已刷新', 'success');
    }
    
    /**
     * 导出统计报告
     */
    exportReport() {
        if (!this.statisticsData) {
            Utils.showToast('暂无数据可导出', 'warning');
            return;
        }
        
        const reportData = {
            生成时间: Utils.formatDateTime(new Date()),
            学生总数: this.statisticsData.totalStudents,
            专业数量: this.statisticsData.totalMajors,
            平均年龄: this.statisticsData.averageAge,
            今日新增: this.statisticsData.todayNew
        };
        
        const csvContent = this.convertToCSV(reportData);
        this.downloadCSV(csvContent, `学生统计报告_${Utils.formatDate(new Date())}.csv`);
    }
    
    /**
     * 转换为CSV格式
     */
    convertToCSV(data) {
        const headers = Object.keys(data).join(',');
        const values = Object.values(data).join(',');
        return `${headers}\n${values}`;
    }
    
    /**
     * 下载CSV文件
     */
    downloadCSV(content, filename) {
        const blob = new Blob([content], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        
        if (link.download !== undefined) {
            const url = URL.createObjectURL(blob);
            link.setAttribute('href', url);
            link.setAttribute('download', filename);
            link.style.visibility = 'hidden';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
    }
}

// 创建仪表盘管理器实例
let dashboardManager;
document.addEventListener('DOMContentLoaded', () => {
    dashboardManager = new DashboardManager();
    window.dashboardManager = dashboardManager;
});
```

---

## 6.8 应用主入口

### 🎯 实战任务9：创建应用主控制器

```javascript
// src/main/resources/static/js/app.js

/**
 * 应用主控制器
 */
class Application {
    constructor() {
        this.currentSection = 'dashboard';
        this.managers = {};
        
        this.initializeApplication();
    }
    
    /**
     * 初始化应用
     */
    initializeApplication() {
        this.setupEventListeners();
        this.loadUserPreferences();
        this.showSection(this.currentSection);
        
        // 显示欢迎消息
        setTimeout(() => {
            Utils.showToast('欢迎使用学生管理系统！', 'success');
        }, 1000);
    }
    
    /**
     * 设置事件监听器
     */
    setupEventListeners() {
        // 导航菜单点击事件
        document.addEventListener('click', (e) => {
            const navLink = e.target.closest('[data-section]');
            if (navLink) {
                e.preventDefault();
                const section = navLink.dataset.section;
                this.showSection(section);
                this.updateActiveNavigation(navLink);
            }
        });
        
        // 键盘快捷键
        document.addEventListener('keydown', (e) => {
            this.handleKeyboardShortcuts(e);
        });
        
        // 窗口大小变化事件
        window.addEventListener('resize', 
            Utils.throttle(() => this.handleWindowResize(), 250)
        );
        
        // 页面可见性变化事件
        document.addEventListener('visibilitychange', () => {
            if (!document.hidden) {
                this.handlePageVisible();
            }
        });
        
        // 页面卸载前保存状态
        window.addEventListener('beforeunload', () => {
            this.saveUserPreferences();
        });
    }
    
    /**
     * 显示指定部分
     */
    showSection(sectionName) {
        // 隐藏所有部分
        document.querySelectorAll('.content-section').forEach(section => {
            section.classList.add('d-none');
        });
        
        // 显示指定部分
        const targetSection = document.getElementById(`${sectionName}-section`);
        if (targetSection) {
            targetSection.classList.remove('d-none');
            targetSection.classList.add('fade-in');
            
            // 移除动画类（避免重复动画）
            setTimeout(() => {
                targetSection.classList.remove('fade-in');
            }, 500);
        }
        
        this.currentSection = sectionName;
        
        // 根据部分执行特定操作
        this.handleSectionChange(sectionName);
    }
    
    /**
     * 处理部分切换
     */
    handleSectionChange(sectionName) {
        switch (sectionName) {
            case 'dashboard':
                if (window.dashboardManager) {
                    // 刷新仪表盘数据
                    setTimeout(() => {
                        window.dashboardManager.loadDashboardData();
                    }, 100);
                }
                break;
                
            case 'students':
                if (window.studentManager) {
                    // 刷新学生列表
                    setTimeout(() => {
                        window.studentManager.loadStudents();
                    }, 100);
                }
                break;
                
            case 'statistics':
                // 加载统计页面的图表
                this.loadStatisticsCharts();
                break;
        }
    }
    
    /**
     * 加载统计图表
     */
    async loadStatisticsCharts() {
        try {
            // 这里可以加载更详细的统计图表
            if (window.dashboardManager) {
                await window.dashboardManager.loadAgeDistribution();
                await window.dashboardManager.loadEnrollmentYearData();
            }
        } catch (error) {
            console.error('加载统计图表失败:', error);
        }
    }
    
    /**
     * 更新导航状态
     */
    updateActiveNavigation(activeLink) {
        // 移除所有活动状态
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
        });
        
        // 添加活动状态
        activeLink.classList.add('active');
    }
    
    /**
     * 处理键盘快捷键
     */
    handleKeyboardShortcuts(e) {
        // Ctrl/Cmd + 数字键切换页面
        if ((e.ctrlKey || e.metaKey) && !e.shiftKey && !e.altKey) {
            switch (e.key) {
                case '1':
                    e.preventDefault();
                    this.showSection('dashboard');
                    break;
                case '2':
                    e.preventDefault();
                    this.showSection('students');
                    break;
                case '3':
                    e.preventDefault();
                    this.showSection('statistics');
                    break;
                case 'r':
                    e.preventDefault();
                    this.refreshCurrentSection();
                    break;
            }
        }
        
        // ESC键关闭模态框
        if (e.key === 'Escape') {
            const openModal = document.querySelector('.modal.show');
            if (openModal) {
                const modal = bootstrap.Modal.getInstance(openModal);
                if (modal) {
                    modal.hide();
                }
            }
        }
    }
    
    /**
     * 刷新当前部分
     */
    refreshCurrentSection() {
        switch (this.currentSection) {
            case 'dashboard':
                if (window.dashboardManager) {
                    window.dashboardManager.refresh();
                }
                break;
                
            case 'students':
                if (window.studentManager) {
                    window.studentManager.loadStudents();
                    Utils.showToast('学生列表已刷新', 'success');
                }
                break;
                
            case 'statistics':
                this.loadStatisticsCharts();
                Utils.showToast('统计数据已刷新', 'success');
                break;
        }
    }
    
    /**
     * 处理窗口大小变化
     */
    handleWindowResize() {
        // 重新调整图表大小
        if (window.chartService) {
            window.chartService.charts.forEach(chart => {
                if (chart && typeof chart.resize === 'function') {
                    chart.resize();
                }
            });
        }
    }
    
    /**
     * 处理页面重新可见
     */
    handlePageVisible() {
        // 页面重新可见时刷新数据
        if (this.currentSection === 'dashboard' && window.dashboardManager) {
            window.dashboardManager.loadStatistics();
        }
    }
    
    /**
     * 加载用户偏好设置
     */
    loadUserPreferences() {
        const preferences = Utils.getFromStorage(CONFIG.STORAGE_KEYS.USER_PREFERENCES, {
            theme: 'light',
            language: 'zh-CN',
            pageSize: CONFIG.PAGINATION.DEFAULT_PAGE_SIZE
        });
        
        this.applyUserPreferences(preferences);
    }
    
    /**
     * 应用用户偏好设置
     */
    applyUserPreferences(preferences) {
        // 应用主题
        if (preferences.theme === 'dark') {
            document.body.classList.add('dark-theme');
        }
        
        // 应用语言设置
        document.documentElement.lang = preferences.language;
        
        // 应用分页大小
        if (window.studentManager) {
            window.studentManager.pageSize = preferences.pageSize;
        }
    }
    
    /**
     * 保存用户偏好设置
     */
    saveUserPreferences() {
        const preferences = {
            theme: document.body.classList.contains('dark-theme') ? 'dark' : 'light',
            language: document.documentElement.lang || 'zh-CN',
            pageSize: window.studentManager ? window.studentManager.pageSize : CONFIG.PAGINATION.DEFAULT_PAGE_SIZE,
            lastSection: this.currentSection
        };
        
        Utils.saveToStorage(CONFIG.STORAGE_KEYS.USER_PREFERENCES, preferences);
    }
    
    /**
     * 显示帮助信息
     */
    showHelp() {
        const helpContent = `
            <h5>键盘快捷键</h5>
            <ul>
                <li><kbd>Ctrl/Cmd + 1</kbd> - 切换到仪表盘</li>
                <li><kbd>Ctrl/Cmd + 2</kbd> - 切换到学生管理</li>
                <li><kbd>Ctrl/Cmd + 3</kbd> - 切换到统计分析</li>
                <li><kbd>Ctrl/Cmd + R</kbd> - 刷新当前页面</li>
                <li><kbd>ESC</kbd> - 关闭模态框</li>
            </ul>
            
            <h5>功能说明</h5>
            <ul>
                <li>仪表盘：查看学生统计信息和图表</li>
                <li>学生管理：添加、编辑、删除学生信息</li>
                <li>统计分析：查看详细的数据分析</li>
            </ul>
        `;
        
        // 这里可以显示帮助模态框
        Utils.showToast('按 F1 查看完整帮助文档', 'info');
    }
}

// 应用启动
document.addEventListener('DOMContentLoaded', () => {
    window.app = new Application();
    
    // 全局错误处理
    window.addEventListener('error', (e) => {
        console.error('全局错误:', e.error);
        Utils.showToast('系统发生错误，请刷新页面重试', 'error');
    });
    
    // 全局未处理的Promise拒绝
    window.addEventListener('unhandledrejection', (e) => {
        console.error('未处理的Promise拒绝:', e.reason);
        Utils.showToast('网络请求失败，请检查网络连接', 'error');
    });
});
```

---

## 6.9 知识扩展

### 📚 前端性能优化

#### 代码分割与懒加载
```javascript
// 动态导入模块
const loadChartModule = async () => {
    const { ChartService } = await import('./charts.js');
    return new ChartService();
};

// 图片懒加载
const lazyImages = document.querySelectorAll('img[data-src]');
const imageObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            const img = entry.target;
            img.src = img.dataset.src;
            img.classList.remove('lazy');
            imageObserver.unobserve(img);
        }
    });
});

lazyImages.forEach(img => imageObserver.observe(img));
```

#### 缓存策略
```javascript
// Service Worker缓存
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/sw.js')
        .then(registration => console.log('SW注册成功'))
        .catch(error => console.log('SW注册失败'));
}

// 内存缓存
class CacheManager {
    constructor() {
        this.cache = new Map();
        this.maxSize = 100;
    }
    
    set(key, value, ttl = 300000) { // 5分钟TTL
        if (this.cache.size >= this.maxSize) {
            const firstKey = this.cache.keys().next().value;
            this.cache.delete(firstKey);
        }
        
        this.cache.set(key, {
            value,
            expires: Date.now() + ttl
        });
    }
    
    get(key) {
        const item = this.cache.get(key);
        if (!item) return null;
        
        if (Date.now() > item.expires) {
            this.cache.delete(key);
            return null;
        }
        
        return item.value;
    }
}
```

### 🔒 前端安全最佳实践

#### XSS防护
```javascript
// HTML转义
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// CSP策略
// 在HTML头部添加：
// <meta http-equiv="Content-Security-Policy" 
//       content="default-src 'self'; script-src 'self' 'unsafe-inline'">
```

#### CSRF防护
```javascript
// CSRF Token
axios.defaults.headers.common['X-CSRF-TOKEN'] = 
    document.querySelector('meta[name="csrf-token"]').getAttribute('content');
```

### 📱 响应式设计进阶

#### 移动端适配
```css
/* 触摸友好的按钮 */
.btn-touch {
    min-height: 44px;
    min-width: 44px;
    padding: 12px 16px;
}

/* 移动端导航 */
@media (max-width: 768px) {
    .navbar-collapse {
        position: fixed;
        top: 0;
        left: -100%;
        width: 80%;
        height: 100vh;
        background: white;
        transition: left 0.3s ease;
        z-index: 1050;
    }
    
    .navbar-collapse.show {
        left: 0;
    }
}
```

---

## 6.10 实战练习

### 🎯 练习1：添加暗黑主题
**任务**：为应用添加暗黑主题切换功能

**要求**：
1. 添加主题切换按钮
2. 实现CSS变量切换
3. 保存用户主题偏好
4. 适配所有组件

### 🎯 练习2：实现数据导出功能
**任务**：添加学生数据导出为Excel功能

**要求**：
1. 支持导出当前筛选结果
2. 支持自定义导出字段
3. 添加导出进度提示
4. 处理大数据量导出

### 🎯 练习3：添加实时通知
**任务**：实现WebSocket实时通知功能

**要求**：
1. 连接WebSocket服务
2. 显示实时通知
3. 处理连接断开重连
4. 通知历史记录

---

## 📝 本章总结

通过本章学习，你已经掌握了：

### ✅ 核心技能
- **现代前端技术栈**：HTML5、CSS3、JavaScript ES6+
- **响应式设计**：Bootstrap框架、移动端适配
- **组件化开发**：模块化JavaScript、可复用组件
- **数据可视化**：Chart.js图表库应用
- **用户体验**：交互设计、动画效果

### ✅ 实践能力
- **项目架构设计**：前端分层架构、模块划分
- **API集成**：Axios HTTP客户端、错误处理
- **状态管理**：本地存储、用户偏好
- **性能优化**：懒加载、缓存策略
- **安全防护**：XSS防护、CSRF防护

### ✅ 工程化思维
- **代码组织**：模块化、可维护性
- **错误处理**：全局错误捕获、用户友好提示
- **用户体验**：加载状态、空状态处理
- **可访问性**：键盘导航、屏幕阅读器支持

### 🎯 下一步学习建议
1. **深入学习现代前端框架**（Vue.js、React）
2. **掌握前端工程化工具**（Webpack、Vite）
3. **学习TypeScript**提升代码质量
4. **了解微前端架构**适应大型项目
5. **关注Web性能优化**技术

---

## 🔗 参考资源

- [MDN Web文档](https://developer.mozilla.org/)
- [Bootstrap官方文档](https://getbootstrap.com/)
- [Chart.js文档](https://www.chartjs.org/)
- [Axios文档](https://axios-http.com/)
- [Web性能优化指南](https://web.dev/performance/)

---

**恭喜！你已经完成了前端开发章节的学习。现在你具备了构建现代Web应用前端的能力，可以继续学习下一章节的内容。**
```